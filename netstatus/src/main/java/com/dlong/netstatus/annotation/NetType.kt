package com.dlong.netstatus.annotation

/**
 * 网络状态
 *
 * @author D10NG
 * @date on 2019-10-21 17:23
 */
@Target(AnnotationTarget.TYPE)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class NetType {

    companion object {
        // wifi
        const val WIFI = "WIFI"
        // 手机网络 2G
        const val NET_2G = "NET_2G"
        // 手机网络 3G
        const val NET_3G = "NET_3G"
        // 手机网络 4G
        const val NET_4G = "NET_4G"
        // 手机网络 5G
        const val NET_5G = "NET_5G"
        // 未识别网络
        const val NET_UNKNOWN = "NET_UNKNOWN"
        // 没有网络
        const val NONE = "NONE"
    }
}