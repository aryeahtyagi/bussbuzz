package com.fortradestudio.busbuzz.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.fortradestudio.busbuzz.MainActivity
import com.fortradestudio.busbuzz.R
import com.fortradestudio.busbuzz.location.Location
import com.fortradestudio.busbuzz.location.LocationRepositoryImpl
import java.util.*


private const val TAG = "ReminderService"

class ReminderService : Service() {

    private var isPlaying:Boolean = false
    private val uri by lazy {
        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
    }
    private val ring by lazy {
        RingtoneManager.getRingtone(applicationContext, uri)
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val locationRepo = LocationRepositoryImpl()
        val target = intent?.getStringExtra("target")
        val minDis = intent?.getIntExtra("minDis", -1)

        val location = target?.split("-") as List<String>

        Log.i(TAG, "onStartCommand: target $target")
        Log.i(TAG, "onStartCommand: min dis $minDis")

        if(!isPlaying) {

            locationRepo.getUserLocationCoordinates(applicationContext,
                Location(location[0].toDouble(), location[1].toDouble()),
                10_000, 5000, {
                    // it is user's current location
                }) {
                // it is user current distance from target location
                if (it.div(1000) <= minDis!!) {

                    Log.i(TAG, "onStartCommand: distance calculated -> ${it.div(1000)}")
                    Log.i(TAG, "onStartCommand: minimum distance spe ->$minDis")
                    
                        if(!isPlaying) {
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                                ring.isLooping = true
                                ring.play()
                                startForeground(1,notificationBuilder())

                            }else{
                                val timer = Timer()
                                timer.scheduleAtFixedRate(object : TimerTask() {
                                    override fun run() {
                                        if (!ring.isPlaying()) {
                                            ring.play()
                                        }
                                    }
                                }, 1000, 100)
                            }
                            isPlaying = true
                        }
                }
            }

        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        ring.stop()
        stopSelf()
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun notificationBuilder():Notification{

        val intent = Intent(this,MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            this,11,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this,"Reminder channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setContentTitle("Wake Up !! ")
            .setContentText("Your Destination has arrived !")
            .setAutoCancel(true)
            .setColor(Color.RED)
            .addAction(
                NotificationCompat.Action(
                    R.drawable.ic_baseline_close_24,
                "Stop Buzz",
                    pendingIntent)
            )

//
//        val notificationManager = ContextCompat.getSystemService(this,NotificationManager::class.java) as NotificationManager
//        notificationManager.notify()
        return builder.build()
    }



}