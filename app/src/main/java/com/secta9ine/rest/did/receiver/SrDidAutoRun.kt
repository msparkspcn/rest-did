package com.secta9ine.rest.did.receiver

import android.app.ActivityManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.secta9ine.rest.did.MainActivity
import java.util.TreeMap

class SrDidAutoRun : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("SrDidAutoRun", "onReceive called")
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            val packageName = context.packageName
            if (!isAppRunning(context, packageName)) {
                Log.d("SrDidAutoRun", "not opened")
                val it = Intent(context, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(it)
            } else {
                Log.d("SrDidAutoRun", "opened")
            }
        }
    }

    private fun isAppRunning(context: Context, packageName: String): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningAppProcesses = activityManager.runningAppProcesses
        for (process in runningAppProcesses) {
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