package com.dlong.netstatus.impl

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import com.dlong.netstatus.annotation.DLNet
import com.dlong.netstatus.annotation.NetType
import com.dlong.netstatus.constant.Constants
import com.dlong.netstatus.utils.NetUtils
import java.lang.Exception
import java.lang.RuntimeException
import java.lang.reflect.Method
import java.util.HashMap

/**
 * 网络监听
 *
 * @author D10NG
 * @date on 2019-10-22 11:13
 */
class NetStatusCallBack constructor(
    application: Application
) : ConnectivityManager.NetworkCallback() {

    // 观察者，key=类、value=方法
    private val checkManMap = HashMap<Any, Method>()
    // 网络状态广播监听
    private val receiver = NetStatusReceiver()

    // 网络状态记录
    @Volatile
    private var netType : @NetType String = NetType.NET_UNKNOWN
    private var netWork: Network? = null

    init {
        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        application.registerReceiver(receiver, filter)
        netType = NetUtils.getNetStatus(application)
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        Log.i(Constants.TAG, "net connect success! 网络已连接")
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        Log.i(Constants.TAG, "net disconnect! 网络已断开连接")
        post(NetType.NONE)
    }

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities)
        Log.i(Constants.TAG, "net status change! 网络连接改变")
        // 表明此网络连接成功验证
        val type = NetUtils.getNetStatus(networkCapabilities)
        if (type == netType) return
        post(type)
    }

    // 执行
    private fun post(str: @NetType String) {
        netType = str
        val set: Set<Any> = checkManMap.keys
        for (obj in set) {
            val method = checkManMap[obj] ?: continue
            invoke(obj, method, str)
        }
    }

    // 反射执行
    private fun invoke(obj: Any, method: Method, type: @NetType String) {
        try {
            method.invoke(obj, type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 注册
    fun register(obj: Any) {
        val clz = obj.javaClass
        if (!checkManMap.containsKey(clz)) {
            val method = findAnnotationMethod(clz) ?: return
            checkManMap[obj] = method
        }
    }

    // 取消注册
    fun unRegister(obj: Any) {
        checkManMap.remove(obj)
    }

    // 取消所有注册
    fun unRegisterAll() {
        checkManMap.clear()
    }

    // 获取状态
    fun getNetType() : @NetType String = netType

    // 查找监听的方法
    private fun findAnnotationMethod(clz: Class<*>) : Method? {
        val method = clz.methods
        for (m in method) {
            // 看是否有注解
            m.getAnnotation(DLNet::class.java) ?: continue
            // 判断返回类型
            val genericReturnType = m.genericReturnType.toString()
            if ("void" != genericReturnType) {
                // 方法的返回类型必须为void
                throw RuntimeException("The return type of the method【${m.name}】 must be void!")
            }
            // 检查参数，必须有一个String型的参数
            val parameterTypes = m.genericParameterTypes
            if (parameterTypes.size != 1 || parameterTypes[0].toString() != "class ${String::class.java.name}") {
                throw RuntimeException("The parameter types size of the method【${m.name}】must be one (type name must be java.lang.String)!")
            }
            return m
        }
        return null
    }

    inner class NetStatusReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            context?: return
            intent?: return
            val type = NetUtils.getNetStatus(context)
            if (type == netType) return
            post(type)
        }
    }
}