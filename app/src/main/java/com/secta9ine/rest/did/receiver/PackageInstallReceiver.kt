package com.secta9ine.rest.did.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class PackageInstallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_PACKAGE_ADDED) {
            val packageName = intent.data?.encodedSchemeSpecificPart
            if (packageName == context.packageName) {
                // 앱이 설치된 후 자동으로 실행
                Log.d("PackageInstallReceiver","앱 재실행")
                val launchIntent = packageName?.let { context.packageManager.getLaunchIntentForPackage(it) }
                launchIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(launchIntent)
            }
        }
    }
}