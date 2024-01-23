package com.dn.vdp.base_mvvm.presentation.service

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.dn.vdp.base_mvvm.R
import com.dn.vdp.base_mvvm.data.local.prefrence.SharedPrefs

//import com.app.music.data.model.SharedPrefs


class NotificationService:Service() {
    val screenReceiver = ScreenReceiver()
    private val mRemind = "remind"
    lateinit var notificationManager: NotificationManager
    // Constants
    private val SERVICE_ID = 1
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createNotificationChannel(
                notificationManager
            ) else ""
        val notificationBuilder = NotificationCompat.Builder(this, channelId!!)
        val notification: Notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.drawable.ic_app)
            .setPriority(Notification.PRIORITY_MIN)
            .setContentTitle("Task Manager")
            .setContentText("My Task")
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
        startForeground(SERVICE_ID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.intent.action.NOTIFICATION_MUSIC")
        intentFilter.addAction("android.intent.action.REMIND_MUSIC")
        intentFilter.addAction("android.intent.action.START_ALARM")
        intentFilter.addAction("android.intent.action.STOP_ALARM")
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


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager): String? {
        val channelId = "my_service_channelid"
        val channelName = "My Foreground Service"
        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        // omitted the LED color
        channel.importance = NotificationManager.IMPORTANCE_NONE
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        notificationManager.createNotificationChannel(channel)
        return channelId
    }

}