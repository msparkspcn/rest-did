package com.secta9ine.rest.did.util

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInstaller
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import com.secta9ine.rest.did.receiver.UpdateReceiver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import javax.inject.Inject

class VersionUpdater @Inject constructor(

) {
    private val tag = this.javaClass.simpleName
    suspend fun download(apkUrl: String, onDownloaded: (File) -> Unit
    ) = withContext(Dispatchers.IO) {
        try {
            val url = URL(apkUrl)
            val connection = url.openConnection()
            connection.connect()

            val inputStream = BufferedInputStream(url.openStream())
            val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!downloadDir.exists()) {
                downloadDir.mkdirs()
            }

            val apkFile = File(downloadDir, "update.apk")
            val outputStream = FileOutputStream(apkFile)

            val buffer = ByteArray(1024)
            var count: Int
            while (inputStream.read(buffer).also { count = it } != -1) {
                outputStream.write(buffer, 0, count)
            }

            outputStream.flush()
            outputStream.close()
            inputStream.close()

            withContext(Dispatchers.Main) {
                onDownloaded(apkFile)   // 파일 다운로드 완료 후 콜백 호출
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun installApk(context: Context, apkFile: File) {
        Log.d(tag,"installApk")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.d(tag,"기준 버전 이상")
            // ADB를 통해 APK 설치
            val packageInstaller = context.packageManager.packageInstaller
            val sessionParams = PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL)
            val sessionId = packageInstaller.createSession(sessionParams)

            val session = packageInstaller.openSession(sessionId)
            try {
                val inputStream = FileInputStream(apkFile)
                val outStream = session.openWrite("package", 0, apkFile.length())

                inputStream.use { input ->
                    outStream.use { output ->
                        input.copyTo(output)
                        session.fsync(output) // 세션에 작성한 내용을 동기화
                    }
                }

                // 세션 커밋을 위한 PendingIntent 생성
                val intent = Intent(context, UpdateReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(context, sessionId, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

                try {
                    session.commit(pendingIntent.intentSender)
                } catch (e: Exception) {
                    Log.e("InstallAPK", "Error committing session", e)
                }
            } catch (e: IOException) {
                Log.e("InstallAPK", "Error writing APK to session", e)
            } finally {
                session.close() // 세션을 닫아줍니다.
            }
        } else {
            // Android 7.0 미만에서는 직접 설치
            Log.d(tag,"직접 설치")
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}