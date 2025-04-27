package com.secta9ine.rest.did.receiver

import android.app.admin.DeviceAdminReceiver
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInstaller
import android.util.Log
import android.widget.Toast

class UpdateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val status = intent.getIntExtra(PackageInstaller.EXTRA_STATUS, -1)
        val message = intent.getStringExtra(PackageInstaller.EXTRA_STATUS_MESSAGE)
        Log.d("UpdateReceiver", "설치 상태: $status, 메시지: $message")

        when (status) {
            PackageInstaller.STATUS_SUCCESS -> {
                Log.d("UpdateReceiver", "설치 성공")
                // 이 시점에 getPackageInfo() 호출해야 정확해
            }
            PackageInstaller.STATUS_FAILURE -> {
                Log.e("UpdateReceiver", "❌ 설치 실패: $message")
            }
            PackageInstaller.STATUS_PENDING_USER_ACTION -> {
                Log.d("UpdateReceiver", "⚠️ 사용자 액션 필요: 인텐트 요청")
                val activityIntent = intent.getParcelableExtra<Intent>(Intent.EXTRA_INTENT)
                activityIntent?.let {
                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(it)
                }
            }
            else -> {
                Log.e("UpdateReceiver", "❓ 기타 상태: $status, 메시지: $message")
            }
        }
    }
}
