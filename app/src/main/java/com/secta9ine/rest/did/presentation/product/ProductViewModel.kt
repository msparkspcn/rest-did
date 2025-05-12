package com.secta9ine.rest.did.presentation.product

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInstaller
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.secta9ine.rest.did.domain.model.Device
import com.secta9ine.rest.did.domain.model.Product
import com.secta9ine.rest.did.domain.repository.DataStoreRepository
import com.secta9ine.rest.did.domain.repository.DeviceRepository
import com.secta9ine.rest.did.receiver.UpdateReceiver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
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
            Log.d(TAG,"### 상품 화면 deviceId:$deviceId")
            device = deviceRepository.getDevice(deviceId).first()
            Log.d(TAG,"### 상품 화면 device:$device")
            displayCd = device.displayMenuCd!!
            rollingYn = device.rollingYn!!
            productList = listOf(
                Product(
                    productNm = "탐라 흑돼지 김치찌개",
                    productEngNm = "Tamra BlackPork Kimchi Jjigae",
                    price = 7500,
                    productExpln = "탐라의 깊은 맛을 담은 진한 흑돼지 김치찌개",
                    calorie = "365",
                    imgPath = "https://i.namu.wiki/i/aKNgsjY5dvXBsKQkE4DPnvGoLCuE5XBMpObIFQHFHO_CAapAu-UsstNAMdQK-KIKgAfChx76FHaCs--xJrliyFZGujctT1Mkovb8xNxXGvT8_tbP55IyGh9cx09Xk4d2gE0M9PrK1k4mimO2j-riOg.webp"
                ),
                Product(
                    productNm = "제주 고기국수",
                    productEngNm = "Jeju Meat Noodles",
                    price = 8500,
                    calorie = "600",
                    imgPath = "https://www.jejunews.com/news/photo/202201/2189019_211318_325.jpg"
                ),
                Product(
                    productNm = "제주 흑돼지 갈비",
                    productEngNm = "Jeju Black Pork Ribs",
                    price = 9500,
                    calorie = "1000",
                    imgPath = "https://thumbnail10.coupangcdn.com/thumbnails/remote/492x492ex/image/vendor_inventory/ce64/9d0915b99fed846bffbbf01e72847880966f0a80172544284d3a0484f2cf.jpg"
                ),
                Product(
                    productNm = "올레길 비빔밥",
                    productEngNm = "Olleh Trail Bibimbap",
                    price = 12000,
                    calorie = "800",
                    imgPath = "https://img.etoday.co.kr/pto_db/2019/07/20190726153503_1350707_1200_876.jpg"
                ),
                Product(
                    productNm = "한라산 백숙",
                    productEngNm = "Hallasan Chicken Soup",
                    price = 13500,
                    imgPath = "https://shop.hansalim.or.kr/shopping/is/fo/img/%EC%B0%B9%EC%8C%80%EB%88%84%EB%A3%BD%EC%A7%80%EB%B0%B1%EC%88%99_1.jpg"
                ),
                Product(
                    productNm = "제주 감귤 빙수",
                    productEngNm = "Jeju Tangerine Bingsu",
                    price = 6000,
                    imgPath = "https://lounge.josunhotel.com/wp-content/uploads/2022/05/josun-bingsu-2.jpg"
                ),
                Product(
                    productNm = "제주도 전복죽",
                    productEngNm = "Jeju Abalone Porridge",
                    price = 18000,
                    imgPath = "https://i.namu.wiki/i/vBl7qtWof-daNBn8BMF0vAYXQoobrrYsOLyzVx68DZCABG4qTtkhQzOnT0JlHh7lIcr3R8MI10LX1GoqEh1DwA.webp"
                ),
                Product(
                    productNm = "흑돼지 스테이크",
                    productEngNm = "Black Pig Steak",
                    price = 22000,
                    imgPath = "https://previews.123rf.com/images/lenyvavsha/lenyvavsha1704/lenyvavsha170400166/75488495-%EA%B5%AC%EC%9A%B4-%EB%90%9C-%EB%8F%BC%EC%A7%80-%EA%B3%A0%EA%B8%B0-%EC%8A%A4%ED%85%8C%EC%9D%B4%ED%81%AC%EC%99%80-%EB%BC%88-%EC%A0%91%EC%8B%9C%EC%97%90-%EC%8B%A0%EC%84%A0%ED%95%9C-%EC%95%BC%EC%B1%84-%EC%83%90%EB%9F%AC%EB%93%9C-%EA%B7%BC%EC%A0%91-%EC%88%98%ED%8F%89.jpg"
                ),
                Product(
                    productNm = "제주 연어회",
                    productEngNm = "Jeju Salmon Sashimi",
                    price = 17000,
                    imgPath = ""
                ),
                Product(
                    productNm = "서귀포 한우 갈비찜",
                    productEngNm = "Seogwipo Hanwoo Braised Ribs",
                    price = 25000,
                    imgPath = ""
                ),
                Product(
                    productNm = "흑돼지 삼겹살",
                    productEngNm = "Black Pig Samgubsal",
                    price = 25000,
                    imgPath = ""
                )
            )
        }
    }

    fun onEnterKeyPressed(context: Context) {
        Log.d(TAG,"장비설정화면 이동")
        viewModelScope.launch {
            isSystemApp(context)
            val apkUrl = "http://o2pos.spcnetworks.kr/files/app/o2pos/download/backup/1123.apk"

            downloadApkToExternal(context,apkUrl) { downloadedFile ->
                installApk(context, downloadedFile)}
            _uiState.emit(UiState.NavigateToDevice)
        }
    }

    private fun installApk(context: Context, apkFile: File) {
        Log.d(TAG,"installApk")
        // Android 7.0 (API 24) 이상에서는 FileProvider를 사용해야 하지만, 이 방법은 직접 경로를 사용합니다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.d(TAG,"기준 버전 이상")
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
            Log.d(TAG,"직접 설치")
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    suspend fun getDisplayCd(): String {
        val device = deviceRepository.getDevice(
            dataStoreRepository.getDeviceId().first()
        ).firstOrNull() ?: throw RuntimeException("")
        return device.displayMenuCd!!

    }
    private fun downloadApkToExternal(context: Context, apkUrl: String, onDownloaded: (File) -> Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG,"권한 요청")
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1001
            )
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    Log.d(TAG,"다운로드")
                    val url = URL(apkUrl)
                    val connection = url.openConnection()
                    connection.connect()

                    val inputStream = BufferedInputStream(url.openStream())
                    val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    if (!downloadDir.exists()) {
                        downloadDir.mkdirs()
                    }

                    val apkFile = File(downloadDir, "1123.apk")
                    val outputStream = FileOutputStream(apkFile)

                    val data = ByteArray(1024)
                    var count: Int
                    while (inputStream.read(data).also { count = it } != -1) {
                        outputStream.write(data, 0, count)
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
        }
    }

    private fun isSystemApp(context: Context): Boolean {
        return try {
            val appInfo = context.packageManager
                .getApplicationInfo(context.packageName, 0)
            val isSystem = appInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
            val isUpdatedSystem = appInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP != 0
            Log.d(TAG, "FLAG_SYSTEM: $isSystem")
            Log.d(TAG, "FLAG_UPDATED_SYSTEM_APP: $isUpdatedSystem")
            if (isSystem || isUpdatedSystem) {
                Log.i(TAG, "앱은 시스템 앱입니다.")
                true
            } else {
                Log.w(TAG, "앱은 시스템 앱이 아닙니다.")
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