package com.secta9ine.rest.did.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.secta9ine.rest.did.MainActivity
import com.secta9ine.rest.did.R

class AutoStartService : Service() {
    private val NOTIFICATION_ID = 1
    private val CHANNEL_ID = "AutoStartServiceChannel"

    override fun onCreate() {
        super.onCreate()
        Log.d("AutoStartService", "onCreate called")
        createNotificationChannel()

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("앱 자동 시작 중")
            .setContentText("앱이 백그라운드에서 시작되고 있습니다.")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val activityIntent = Intent(this, MainActivity::class.java).apply {
            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        try {
            startActivity(activityIntent)
        } catch (e: Exception) {
            Log.e("AutoStartService", "Failed to launch MainActivity", e)
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val updatedNotification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("SR DID 자동 실행 중")
            .setContentText("앱이 자동으로 실행되었습니다.")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        startForeground(NOTIFICATION_ID, updatedNotification)

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "자동 시작 서비스 채널",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java) as NotificationManager
            manager.createNotificationChannel(serviceChannel)
        }
    }
}