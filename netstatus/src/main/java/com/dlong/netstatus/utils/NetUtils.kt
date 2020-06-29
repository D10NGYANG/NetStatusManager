package com.dlong.netstatus.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build
import com.dlong.netstatus.annotation.NetType

/**
 * 网络工具
 *
 * @author D10NG
 * @date on 2019-10-22 10:29
 */
object NetUtils {

    /**
     * 检查当前连接的网络是否为5G WI-FI
     */
    fun is5GWifiConnected(context: Context) : Boolean {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo?: return false
        // 频段
        var frequency = 0
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP) {
            frequency = wifiInfo.frequency
        } else {
            val ssid = wifiInfo.ssid?: return false
            if (ssid.length < 2) return false
            val sid = ssid.substring(1, ssid.length -1)
            for (scan in wifiManager.scanResults) {
                if (scan.SSID == sid) {
                    frequency = scan.frequency
                    break
                }
            }
        }
        return frequency in 4900..5900
    }

    /**
     * 获取当前连接Wi-Fi名
     * # 如果没有定位权限，获取到的名字将为  unknown ssid
     */
    fun getConnectedWifiSSID(context: Context) : String {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo?: return ""
        return wifiInfo.ssid.substring(1, wifiInfo.ssid.length -1)
    }

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

    /**
     * 获取连接中的网络
     */
    fun getActiveNetwork(context: Context) : Network? {
        val manager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return manager.activeNetwork
        }
        return null
    }
}