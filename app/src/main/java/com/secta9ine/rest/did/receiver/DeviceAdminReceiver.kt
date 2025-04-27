package com.secta9ine.rest.did.receiver

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MyDeviceAdminReceiver : DeviceAdminReceiver() {

    override fun onEnabled(context: Context, intent: Intent) {
        Toast.makeText(context, "Device Admin 권한이 활성화되었습니다.", Toast.LENGTH_SHORT).show()
    }

    override fun onDisabled(context: Context, intent: Intent) {
        Toast.makeText(context, "Device Admin 권한이 비활성화되었습니다.", Toast.LENGTH_SHORT).show()
    }

    override fun onPasswordChanged(context: Context, intent: Intent) {
        Toast.makeText(context, "기기 비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show()
    }

    override fun onPasswordFailed(context: Context, intent: Intent) {
        Toast.makeText(context, "잠금 해제 실패!", Toast.LENGTH_SHORT).show()
    }

    override fun onPasswordSucceeded(context: Context, intent: Intent) {
        Toast.makeText(context, "잠금 해제 성공!", Toast.LENGTH_SHORT).show()
    }

    // 필요시 추가적인 콜백도 구현 가능
}
