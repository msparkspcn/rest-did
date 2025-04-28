package com.secta9ine.rest.did.service

import android.app.Service
import android.app.Service.START_NOT_STICKY
import android.content.Intent
import android.os.IBinder
import androidx.core.content.ContextCompat.startActivity
import com.secta9ine.rest.did.MainActivity

class AutoStartService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val activityIntent = Intent(this, MainActivity::class.java)
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(activityIntent)
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}