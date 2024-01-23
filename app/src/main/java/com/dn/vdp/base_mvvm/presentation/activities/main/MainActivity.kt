package com.dn.vdp.base_mvvm.presentation.activities.main

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.dn.vdp.base_module.presentation.BaseActivity
import com.dn.vdp.base_module.utils.viewBindings
import com.dn.vdp.base_mvvm.R
import com.dn.vdp.base_mvvm.databinding.ActivityMainBinding
import com.dn.vdp.base_mvvm.presentation.service.NotificationService
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(R.layout.activity_main),
    ServiceConnection {

    override val binding by viewBindings(ActivityMainBinding::bind)
    override val viewModel by viewModels<MainViewModel>()
    private  val STORAGE_PERMISSION_CODE = 100
    private val MY_REQUEST_CODE = 100
    override fun setupViews() {
        requestPermission()
        checkForUpdate()
    }

   private fun checkForUpdate() {
        val appUpdateManager = AppUpdateManagerFactory.create(this)

// Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                // This example applies an immediate update. To apply a flexible update
                // instead, pass in AppUpdateType.FLEXIBLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                // Request the update.
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    this,
                    MY_REQUEST_CODE
                )
            } else {
                commonLog("update01:Update not available")
            }
        }
    }

    private fun commonLog(message: String) {
        Log.d("tag001", message)
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
    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.POST_NOTIFICATIONS,

                    ),
                STORAGE_PERMISSION_CODE
            )
        } else {
            //Android is below 11(R)
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                STORAGE_PERMISSION_CODE
            )
        }
    }


    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {

    }

    override fun onServiceDisconnected(p0: ComponentName?) {

    }
}