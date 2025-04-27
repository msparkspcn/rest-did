package com.secta9ine.rest.did.presentation.product

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.secta9ine.rest.did.BuildConfig
import com.secta9ine.rest.did.domain.model.Device
import com.secta9ine.rest.did.domain.model.Product
import com.secta9ine.rest.did.domain.repository.DataStoreRepository
import com.secta9ine.rest.did.domain.repository.DeviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


@HiltViewModel
class ProductViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {
    private val TAG = this.javaClass.simpleName
    private val _uiState = MutableSharedFlow<UiState>()
    val uiState = _uiState.asSharedFlow()
    
    var productList by mutableStateOf(emptyList<Product>())
        private set
    var device by mutableStateOf(Device())
    var displayCd: String? = null
    var rollingYn: String = "N"
    init {
        uiState.onEach { Log.d(TAG, "uiState=$it") }.launchIn(viewModelScope)


        viewModelScope.launch {
            val deviceId =dataStoreRepository.getDeviceId().first()
            Log.d(TAG,"###상품화면 deviceId:$deviceId")
            device = deviceRepository.getDevice(deviceId).first()
            Log.d(TAG,"###상품화면 device:$device")
            displayCd = device.displayMenuCd!!
            rollingYn = device.rollingYn!!
            productList = listOf(
                Product(
                    productNm = "탐라 흑돼지 김치찌개",
                    productEngNm = "Tamra BlackPork Kimchi Jjigae",
                    price = 7500,
                    productExpln = "탐라의 깊은 맛을 담은 진한 흑돼지 김치찌개",
                    calorie = "365"
                ),
                Product(
                    productNm = "제주 고기국수",
                    productEngNm = "Jeju Meat Noodles",
                    price = 8500,
                    calorie = "600"

                ),
                Product(
                    productNm = "제주 흑돼지 갈비",
                    productEngNm = "Jeju Black Pork Ribs",
                    price = 9500,
                    calorie = "1000"
                ),
                Product(
                    productNm = "올레길 비빔밥",
                    productEngNm = "Olleh Trail Bibimbap",
                    price = 12000,
                    calorie = "800"
                ),
                Product(
                    productNm = "한라산 백숙",
                    productEngNm = "Hallasan Chicken Soup",
                    price = 13500
                ),
                Product(
                    productNm = "제주 감귤 빙수",
                    productEngNm = "Jeju Tangerine Bingsu",
                    price = 6000
                ),
                Product(
                    productNm = "제주도 전복죽",
                    productEngNm = "Jeju Abalone Porridge",
                    price = 18000
                ),
                Product(
                    productNm = "흑돼지 스테이크",
                    productEngNm = "Black Pig Steak",
                    price = 22000
                ),
                Product(
                    productNm = "제주 연어회",
                    productEngNm = "Jeju Salmon Sashimi",
                    price = 17000
                ),
                Product(
                    productNm = "서귀포 한우 갈비찜",
                    productEngNm = "Seogwipo Hanwoo Braised Ribs",
                    price = 25000
                ),
                Product(
                    productNm = "흑돼지 삼겹살",
                    productEngNm = "Black Pig Samgubsal",
                    price = 25000
                )
            )
        }
    }

    fun onEnterKeyPressed(context: Context) {
        Log.d(TAG,"장비설정화면 이동")
//        viewModelScope.launch {
//            _uiState.emit(UiState.NavigateToDevice)
//        }
        updateVersion(context)
    }

    suspend fun getDisplayCd(): String {
        val device = deviceRepository.getDevice(
            dataStoreRepository.getDeviceId().first()
        ).firstOrNull() ?: throw RuntimeException("")
        return device.displayMenuCd!!

    }

    fun updateVersion(context: Context) {
        Log.d(TAG,"1.3updateVersion")
        isSystemApp(context)
        val request = DownloadManager.Request(Uri.parse("http://o2pos.spcnetworks.kr/files/app/o2pos/download/backup/1123.apk"))
        request.setTitle("App Update")
        request.setDescription("Downloading new version...")
        val apkFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "1123.apk")

        val apkUri = Uri.fromFile(apkFile)
        request.setDestinationUri(apkUri)  // 내부 저장소에 저장

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadId = downloadManager.enqueue(request)

        // 다운로드 완료 후 설치
        // 다운로드가 완료되면 PackageInstaller를 사용하여 APK를 설치합니다.
        // 다운로드 완료를 확인하기 위해 BroadcastReceiver를 등록합니다.
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                Log.d(TAG,"업데이트 버전. 다운로드 완료")
                val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (id == downloadId) {
                    Log.d(TAG,"설치")
                    val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "1123.apk")
//                    installSystemApp(context, file)
                    val apkPath = context.filesDir.absolutePath +"/1123.apk";
                    Log.d(TAG, "apkPath:$apkPath");
                    val apkUri = FileProvider.getUriForFile(context,
                            BuildConfig.APPLICATION_ID + ".fileprovider", File(apkPath))  // 1

                    val intent = Intent(Intent.ACTION_VIEW)   // 2
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)  // 3
                    intent.setDataAndType(apkUri, "application/vnd.android.package-archive")  // 4

                    context.startActivity(intent)
                }
                else {
                    Log.d(TAG,"설치 불가")
                }
            }
        }
        context.registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }


    fun installSystemApp(context: Context, apkFile: File) {
        try {
            // APK 파일을 시스템 폴더에 복사
            copyApkToSystem(context, apkFile)

            // 시스템 앱 설치
            val command = "pm install -r /system/priv-app/SRDID/srdid.apk"
            executeShellCommand(command)

            // 기기 재부팅
            rebootDevice()


            Log.d(TAG, "시스템 앱 업데이트 완료")
        } catch (e: Exception) {
            Log.e(TAG, "시스템 앱 설치 실패", e)
        }
    }

    fun copyApkToSystem(context: Context, apkFile: File) {
        val systemAppDir = File("/system/priv-app/SRDID")
        if (!systemAppDir.exists()) {
            systemAppDir.mkdirs()
        }

        val destinationFile = File(systemAppDir, "srdid.apk")
        apkFile.copyTo(destinationFile, overwrite = true)

        // 권한 설정
        destinationFile.setReadable(true, false)
        destinationFile.setWritable(true, false)
        destinationFile.setExecutable(true, false)

        // 권한 부여 (루팅 필요)
        val command = "chmod 644 ${destinationFile.absolutePath}"
        executeShellCommand(command)
    }

    fun executeShellCommand(command: String) {
        val process = Runtime.getRuntime().exec(arrayOf("su", "-c", command))
        process.waitFor()
    }

    fun rebootDevice() {
        val command = "reboot"
        executeShellCommand(command)
    }

    fun isSystemApp(context: Context): Boolean {
        return try {
            val appInfo = context.packageManager
                .getApplicationInfo(context.packageName, 0)
            val isSystem = appInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
            val isUpdatedSystem = appInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP != 0
            Log.d(TAG, "FLAG_SYSTEM: $isSystem")
            Log.d(TAG, "FLAG_UPDATED_SYSTEM_APP: $isUpdatedSystem")
            if (isSystem || isUpdatedSystem) {
                Log.i(TAG, "✅ 앱은 시스템 앱입니다.")
                true
            } else {
                Log.w(TAG, "❌ 앱은 시스템 앱이 아닙니다.")
                false
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "패키지를 찾을 수 없습니다: " + e.message)
            false
        }
    }
    sealed interface UiState {
        object Loading : UiState
        object UpdateDevice : UiState
        object NavigateToDevice : UiState
        object NavigateToOrderStatus :UiState
        object Idle : UiState
        data class Error(val message: String) : UiState
    }
}