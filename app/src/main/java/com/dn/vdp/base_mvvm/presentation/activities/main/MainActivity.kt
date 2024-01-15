package com.dn.vdp.base_mvvm.presentation.activities.main

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import androidx.activity.viewModels
import com.dn.vdp.base_module.presentation.BaseActivity
import com.dn.vdp.base_module.utils.viewBindings
import com.dn.vdp.base_mvvm.R
import com.dn.vdp.base_mvvm.databinding.ActivityMainBinding
import com.dn.vdp.base_mvvm.presentation.service.NotificationService
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(R.layout.activity_main),
    ServiceConnection {

    override val binding by viewBindings(ActivityMainBinding::bind)
    override val viewModel by viewModels<MainViewModel>()

    override fun setupViews() {
    }

    override fun bindViewModel() {
        viewModel.test()
    }

    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(Intent(this, NotificationService::class.java))
        } else {
            startService(Intent(this, NotificationService::class.java))
        }
    }

    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {

    }

    override fun onServiceDisconnected(p0: ComponentName?) {

    }
}