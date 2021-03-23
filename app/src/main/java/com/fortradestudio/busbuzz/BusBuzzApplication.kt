package com.fortradestudio.busbuzz

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import com.fortradestudio.busbuzz.service.ReminderService

class BusBuzzApplication : Application() {


    override fun onCreate() {
        super.onCreate()

        val channelId = "Reminder channel"
        val channelName = "Reminder Channel to trigger notification when reminder is received"
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val chan = NotificationChannel(
                channelId,
                channelName, NotificationManager.IMPORTANCE_HIGH
            )
            chan.lightColor = Color.BLUE
            chan.importance = NotificationManager.IMPORTANCE_NONE
            chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            service.createNotificationChannel(chan)
        }
    }

}