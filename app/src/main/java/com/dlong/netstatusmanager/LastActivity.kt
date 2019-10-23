package com.dlong.netstatusmanager

import android.Manifest
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
import com.dlong.netstatusmanager.databinding.ActivityLastBinding

class LastActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLastBinding

    override fun onStart() {
        super.onStart()
        DLNetManager.getInstance(this.application).register(this)
    }

    override fun onStop() {
        super.onStop()
        DLNetManager.getInstance(this.application).unRegister(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_last)

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

    @DLNet
    fun onNetStatusChange(str: @NetType String) {
        // 网络状态改变
        Log.e("测试", "Last网络状态改变：${str}")
        binding.netType = str
    }

    fun finish(v: View) {
        finish()
    }
}
