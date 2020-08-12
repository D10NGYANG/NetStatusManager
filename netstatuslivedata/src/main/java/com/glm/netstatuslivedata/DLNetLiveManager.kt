package com.glm.netstatuslivedata

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkRequest
import androidx.lifecycle.MutableLiveData

/**
 * 网络状态管理器
 *
 * @author D10NG
 * @date on 2019-10-21 16:04
 */
class DLNetLiveManager(private val application: Application) {

    // 回调
    private val netStatusCallBack = NetStatusCallBack(application)

    companion object {

        @Volatile
        private var INSTANCE : DLNetLiveManager? = null

        @JvmStatic
        fun instance(application: Application) : DLNetLiveManager {
            val temp = INSTANCE
            if (null != temp) {
                return temp
            }
            synchronized(this) {
                val instance = DLNetLiveManager(application)
                INSTANCE = instance
                return instance
            }
        }
    }

    init {
        val request = NetworkRequest.Builder().build()
        val manager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        manager.registerNetworkCallback(request, netStatusCallBack)
    }

    fun getNetType() : MutableLiveData<@NetType String> {
        return netStatusCallBack.netTypeLiveData
    }
}