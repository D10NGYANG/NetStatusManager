package com.dlong.netstatusmanager

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.dlong.netstatus.DLNetManager
import com.dlong.netstatus.annotation.DLNet
import com.dlong.netstatus.annotation.NetType
import com.dlong.netstatus.utils.NetUtils
import com.dlong.netstatusmanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onStart() {
        super.onStart()
        DLNetManager.getInstance(this.application).register(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.netType = DLNetManager.getInstance(this.application).getNetType()
        // 检查定位权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 101)
        } else {
            if (binding.netType == NetType.WIFI) {
                Log.e("测试", "WI-FI名：${NetUtils.getConnectedWifiSSID(this)}")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        DLNetManager.getInstance(this.application).unRegister(this)
    }

    @DLNet
    fun onNetStatusChange(str: @NetType String) {
        // 网络状态改变
        Log.e("测试", "Main网络状态改变：${str}")
        binding.netType = str
        if (str == NetType.WIFI) {
            if (NetUtils.is5GWifiConnected(this)) {
                Log.e("测试", "这是5G WI-FI")
            } else{
                Log.e("测试", "这是2.4G WI-FI")
            }
            Log.e("测试", "WI-FI名：${NetUtils.getConnectedWifiSSID(this)}")
        }
    }

    fun go(v: View) {
        startActivity(Intent(this, LastActivity::class.java))
    }

    fun goLive(v: View) {
        startActivity(Intent(this, MainLiveActivity::class.java))
    }
}
