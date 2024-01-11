package com.dn.vdp.base_mvvm.presentation.service

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
//import com.app.music.activity.MainActivity
//import com.app.music.utils.Utils.screenOff
//import com.app.music.data.model.SharedPrefs
//import com.app.music.utils.Utils.mHour
//import com.app.music.utils.Utils.mMinute
import com.dn.vdp.base_mvvm.R
import com.dn.vdp.base_mvvm.presentation.activities.main.MainActivity
import java.util.Calendar


class ScreenReceiver : BroadcastReceiver() {

    private var alarmMgr: AlarmManager? = null
    var alarmIntent: PendingIntent? = null
    var alarmRemidIntent: PendingIntent? = null
    var NOTIFICATION_ID = "notification-id"
    var NOTIFICATION = "notification"
    var NOTIFICATION_CHANNEL_ID = "10001"
    var default_notification_channel_id = "default"
    override fun onReceive(context: Context, intent: Intent?) {
        try {
            if (intent != null) {
                if (intent.action.equals(Intent.ACTION_SCREEN_OFF)) {
                   // screenOff = true
                    Log.e("keshav", "ACTION_SCREEN_OFF  ")
                } else if (intent.action.equals(Intent.ACTION_SCREEN_ON)) {
                  //  screenOff = false
                    Log.e("keshav", "ACTION_SCREEN_ON  ")
                } else if (intent.action.equals("android.intent.action.REMIND_MUSIC")) {
                    Log.e("keshav", "android.intent.action.REMIND_MUSIC")
                    setAlarmRemind(context, 16, 12, "android.intent.action.NOTIFICATION_MUSIC")
                } else if (intent.action.equals("android.intent.action.NOTIFICATION_MUSIC")) {
                    Log.e("keshav", "android.intent.action.NOTIFICATION_MUSIC")
                    val alarmSound =
                        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

                    val r = RingtoneManager.getRingtone(context, alarmSound)
                    r.play()

                    val intent = Intent(context, MainActivity::class.java)
                    val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

                    val builder: NotificationCompat.Builder =
                        NotificationCompat.Builder(context, default_notification_channel_id)
                    builder.setContentTitle("Task Manager")
                    builder.setContentText("Please check the quest list")
                    builder.setSmallIcon(R.drawable.ic_app)
                    builder.setSound(alarmSound)
                    builder.setAutoCancel(true)
                    builder.setChannelId(NOTIFICATION_CHANNEL_ID)
                    builder.setContentIntent(pendingIntent)

                    val notificationManager =
                        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    val notification: Notification = builder.build()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val importance = NotificationManager.IMPORTANCE_HIGH
                        val notificationChannel = NotificationChannel(
                            NOTIFICATION_CHANNEL_ID,
                            "NOTIFICATION_CHANNEL_NAME",
                            importance
                        )
                        assert(notificationManager != null)
                        notificationManager!!.createNotificationChannel(notificationChannel)
                    }
                    val id = intent.getIntExtra(NOTIFICATION_ID, 0)
                    assert(notificationManager != null)
                    notificationManager!!.notify(id, notification)

                } else if (intent.action.equals("android.intent.action.START_ALARM")) {
                    val h = intent.extras!!.getString("hour")
                    val m = intent.extras!!.getString("minute")
                    setAlarm(
                        context,
                        h!!.toInt(),
                        m!!.toInt(),
                        "android.intent.action.STOP_MUSIC"
                    )
                } else if (intent.action.equals("android.intent.action.STOP_ALARM")) {
//                    mHour =""
//                    mMinute =""
//                    SharedPrefs.instance.put("hour", "")
//                    SharedPrefs.instance.put("minute", "")
                    if (alarmIntent != null && alarmMgr != null) {
                        alarmMgr?.cancel(alarmIntent!!)
                    }
                } else if (intent.action.equals("android.intent.action.STOP_MUSIC")) {
//                    val musicService = MusicService()
//                    musicService.pausePlay()
//                    mHour =""
//                    mMinute =""
//                    SharedPrefs.instance.put("hour", "")
//                    SharedPrefs.instance.put("minute", "")
                    if (alarmIntent != null && alarmMgr != null) {
                        alarmMgr?.cancel(alarmIntent!!)
                    }
                } else if (intent.action.equals(AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED)) {
                    // reschedule all the exact alarms
                    Log.e("Alarm", "Triggered1")
                }
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }


    private fun setAlarmRemind(context: Context, hour: Int, minute: Int, action: String) {

        if (alarmIntent != null && alarmMgr != null) {
            alarmMgr?.cancel(alarmRemidIntent!!)
        }
        if (alarmMgr == null)
            alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, ScreenReceiver::class.java)
        intent.action = action
        alarmRemidIntent = intent.let { intent ->
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        alarmMgr?.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            alarmRemidIntent!!
        )

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            when {
//                // If permission is granted, proceed with scheduling exact alarms.
//                alarmMgr?.canScheduleExactAlarms()!! -> {
//                    alarmMgr?.setInexactRepeating(
//                        AlarmManager.RTC_WAKEUP,
//                        calendar.timeInMillis,
//                        AlarmManager.INTERVAL_DAY,
//                        alarmRemidIntent!!
//                    )
//                }
//
//                else -> {
//                    // Ask users to go to exact alarm page in system settings.
//                    val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
//             //       intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                    context.startActivity(intent)
//                }
//            }
//        } else {
//                alarmMgr?.setInexactRepeating(
//                    AlarmManager.RTC_WAKEUP,
//                    calendar.timeInMillis,
//                    AlarmManager.INTERVAL_DAY,
//                    alarmRemidIntent!!
//                )
//        }
    }

    private fun setAlarm(context: Context, hour: Int, minute: Int, action: String) {

        if (alarmIntent != null && alarmMgr != null) {
            alarmMgr?.cancel(alarmIntent!!)
        }
        if (alarmIntent == null)
            alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, ScreenReceiver::class.java)
        intent.action = action
        alarmIntent = intent.let { intent ->
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            when {
                // If permission is granted, proceed with scheduling exact alarms.
                alarmMgr?.canScheduleExactAlarms()!! -> {

                        alarmMgr?.setExact(
                            AlarmManager.RTC_WAKEUP,
                            calendar.timeInMillis,
                            alarmIntent!!
                        )
                }

                else -> {
                    // Ask users to go to exact alarm page in system settings.
                    val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                    context.startActivity(intent)
                }
            }
        } else {

                alarmMgr?.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    alarmIntent!!
                )
        }

//        mHour = hour.toString()
//        mMinute = minute.toString()
//        SharedPrefs.instance.put("hour", hour.toString())
//        SharedPrefs.instance.put("minute", minute.toString())
    }
}