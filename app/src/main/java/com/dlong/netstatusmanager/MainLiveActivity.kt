package com.dlong.netstatusmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.dlong.netstatus.DLNetManager
import com.dlong.netstatusmanager.databinding.ActivityMainLiveBinding

class MainLiveActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainLiveBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_live)

        DLNetManager.getInstance(this.application).getNetTypeLiveData().observe(this, Observer {
            binding.netType = it
        })
    }
}