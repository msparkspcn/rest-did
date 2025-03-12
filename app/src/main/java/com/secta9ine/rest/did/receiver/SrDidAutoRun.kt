package com.secta9ine.rest.did.receiver

import android.app.ActivityManager
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import android.util.Log
import androidx.annotation.RequiresApi
import com.secta9ine.rest.did.MainActivity
import java.util.TreeMap


class SrDidAutoRun : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("SrDidAutoRun", "onReceive called")
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            val packageName = context.packageName
            if (!isAppRunning(context, packageName)) {
                Log.d("SrDidAutoRun", "not opened")
                val serviceIntent = Intent(context, AutoStartService::class.java)
                context.startForegroundService(serviceIntent)
//                scheduleAppLaunch(context)

//                val it = Intent(context, MainActivity::class.java).apply {
//                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                }
//                Log.d("SrDidAutoRun", "Starting MainActivity")
//                context.startActivity(it)
            } else {
                Log.d("SrDidAutoRun", "opened")
            }
        }
    }
    private fun scheduleAppLaunch(context: Context) {
        val launchIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager?.setExact(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + 5000,  // 5초 후 실행
            pendingIntent
        )
    }

    private fun isAppRunning(context: Context, packageName: String): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningAppProcesses = activityManager.runningAppProcesses
        Log.d("SrDidAutoRun","packageName:$packageName")
        for (process in runningAppProcesses) {
            Log.d("SrDidAutoRun","packageName2:${process.processName}")
            if (process.processName == packageName && process.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                Log.d("SrDidAutoRun", "${process.processName} is running!!")
                return true
            }
        }
        return false
    }

    private fun getTopPackageNm(context: Context): String? {
        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val currentTime = System.currentTimeMillis()

        val stats: List<UsageStats> = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY, currentTime - 60 * 1000, currentTime
        )

        val sortedStats = TreeMap<Long, UsageStats>()
        for (usageStats in stats) {
            sortedStats[usageStats.lastTimeUsed] = usageStats
        }

        return if (sortedStats.isNotEmpty()) {
            sortedStats[sortedStats.lastKey()]?.packageName
        } else {
            null
        }
    }
}