package com.glm.netstatuslivedata

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.MutableLiveData

/**
 * 网络监听
 *
 * @author D10NG
 * @date on 2019-10-22 11:13
 */
class NetStatusCallBack constructor(
    application: Application
) : ConnectivityManager.NetworkCallback() {

    // 网络状态广播监听
    private val receiver = NetStatusReceiver()
    // 网络状态
    val netTypeLiveData: MutableLiveData<@NetType String> = MutableLiveData()

    init {
        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        application.registerReceiver(receiver, filter)
        netTypeLiveData.postValue(NetUtils.getNetStatus(application))
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        Log.i(Constants.TAG, "net connect success! 网络已连接")
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        Log.i(Constants.TAG, "net disconnect! 网络已断开连接")
        netTypeLiveData.postValue(NetType.NONE)
    }

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities)
        Log.i(Constants.TAG, "net status change! 网络连接改变")
        // 表明此网络连接成功验证
        val type = NetUtils.getNetStatus(networkCapabilities)
        if (type == netTypeLiveData.value) return
        netTypeLiveData.postValue(type)
    }

    inner class NetStatusReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            context?: return
            intent?: return
            val type = NetUtils.getNetStatus(context)
            if (type == netTypeLiveData.value) return
            netTypeLiveData.postValue(type)
        }
    }
}