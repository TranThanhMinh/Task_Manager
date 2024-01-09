package com.dn.vdp.base_mvvm.presentation.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import com.dn.vdp.base_mvvm.data.local.prefrence.SharedPrefs

//import com.app.music.data.model.SharedPrefs


class NotificationService:Service() {
    val screenReceiver = ScreenReceiver()
    private var alarmMgr: AlarmManager? = null
    var alarmIntent: PendingIntent? = null
    private val mRemind = "remind"
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.intent.action.NOTIFICATION_MUSIC")
        intentFilter.addAction("android.intent.action.REMIND_MUSIC")
        registerReceiver(screenReceiver, intentFilter,RECEIVER_EXPORTED)
        val remind = SharedPrefs.instance[mRemind, Int::class.java]
        if(remind == 0){
            sendBroadcast( Intent("android.intent.action.REMIND_MUSIC"))
            SharedPrefs.instance.put(mRemind,1)
        }
        Log.d("onStartCommand",remind.toString())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val intentFilter = IntentFilter()
        intentFilter.addAction("android.intent.action.NOTIFICATION_MUSIC")
        intentFilter.addAction("android.intent.action.REMIND_MUSIC")
        registerReceiver(screenReceiver, intentFilter,RECEIVER_EXPORTED)
        val remind = SharedPrefs.instance[mRemind, Int::class.java]
        if(remind == 0){
            sendBroadcast( Intent("android.intent.action.REMIND_MUSIC"))
            SharedPrefs.instance.put(mRemind,1)
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(screenReceiver)
    }
    override fun onBind(p0: Intent?): IBinder? {
       return null
    }
}