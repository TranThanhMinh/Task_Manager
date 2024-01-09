package com.dn.vdp.base_mvvm.presentation.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import com.dn.vdp.base_mvvm.data.local.prefrence.SharedPrefs

//import com.app.music.data.model.SharedPrefs


class NotificationService:Service() {
    val screenReceiver = ScreenReceiver()
    private var alarmMgr: AlarmManager? = null
    var alarmIntent: PendingIntent? = null
    private val mRemind = "remind"
    override fun onCreate() {
        super.onCreate()
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.intent.action.NOTIFICATION_MUSIC")
        intentFilter.addAction("android.intent.action.REMIND_MUSIC")
        registerReceiver(screenReceiver, intentFilter)
        val remind = SharedPrefs.instance[mRemind, Int::class.java]
        if(remind == 0){
            sendBroadcast( Intent("android.intent.action.REMIND_MUSIC"))
            SharedPrefs.instance.put(mRemind,1)
        }
        Log.d("onStartCommand",remind.toString())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {



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