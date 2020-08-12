package com.glm.netstatuslivedata

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

/**
 * 网络工具
 *
 * @author D10NG
 * @date on 2019-10-22 10:29
 */
object NetUtils {

    /**
     * 获取网络状态
     */
    fun getNetStatus(context: Context) : @NetType String {
        val manager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val net = manager.activeNetwork
            val ties = manager.getNetworkCapabilities(net)?: return NetType.NONE
            return getNetStatus(ties)
        } else {
            val netInfo = manager.activeNetworkInfo
            if (netInfo == null || !netInfo.isAvailable) {
                return NetType.NONE
            }
            return when(netInfo.type) {
                ConnectivityManager.TYPE_MOBILE -> NetType.NET
                ConnectivityManager.TYPE_WIFI -> NetType.WIFI
                else -> NetType.NET_UNKNOWN
            }
        }
    }

    /**
     * 获取网络状态
     */
    fun getNetStatus(ties: NetworkCapabilities) : @NetType String {
        return if (!ties.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
            NetType.NONE
        } else {
            if (ties.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                ties.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE)) {
                // 使用WI-FI
                NetType.WIFI
            } else if (ties.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                ties.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ) {
                // 使用蜂窝网络
                NetType.NET
            } else{
                // 未知网络，包括蓝牙、VPN、LoWPAN
                NetType.NET_UNKNOWN
            }
        }
    }
}