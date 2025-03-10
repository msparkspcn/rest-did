package com.secta9ine.rest.did.util

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log

class CommonUtils {

    companion object {

        private val TAG: String = this.javaClass.simpleName

        fun getAppVersion(context: Context): String {
            var result = ""
            try {
                val pm = context.packageManager
                val pi = pm.getPackageInfo(context.packageName, PackageManager.GET_META_DATA)
                result = pi.versionName
            } catch (e: PackageManager.NameNotFoundException) {
                Log.d(TAG, "### 앱 버전 획득 실패")
                e.printStackTrace()
            }
            return result
        }
    }
}