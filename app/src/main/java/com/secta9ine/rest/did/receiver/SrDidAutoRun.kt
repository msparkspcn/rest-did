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

private val TAG = "SrDidAutoRun"
class SrDidAutoRun : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("SrDidAutoRun", "onReceive called")
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            val packageName = context.packageName
            if (!isAppRunning(context, packageName)) {
                Log.d("SrDidAutoRun", "not opened")

                if (!isAppRunning(context, packageName)) {
                    Log.d(TAG, "not opened")

                    // Android 8.0 (API 26) 이상 startForegroundService 사용
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Log.d(TAG,"Main start1")
                        // Foreground Service를 시작하는 방법
//                        val serviceIntent = Intent(context, AutoStartService::class.java)
//                        context.startForegroundService(serviceIntent)
                    } else {
                        // Android 8.0 미만 Activity 시작
                        Log.d(TAG,"Main start2")
                        val intent = Intent(context, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    }
                } else {
                    Log.d(TAG, "opened")
                }
            } else {
                Log.d("SrDidAutoRun", "opened")
            }
        }
    }

    private fun isAppRunning(context: Context, packageName: String): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningAppProcesses = activityManager.runningAppProcesses
        Log.d("SrDidAutoRun","packageName:$packageName")
        for (process in runningAppProcesses) {
            if (process.processName == packageName &&
                process.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
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