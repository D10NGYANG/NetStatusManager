package com.dlong.netstatus.utils

import android.content.Context
import android.net.wifi.WifiManager

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
}