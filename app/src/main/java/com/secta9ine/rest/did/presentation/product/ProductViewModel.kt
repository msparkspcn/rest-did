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
import com.secta9ine.rest.did.domain.repository.ProductRepository
import com.secta9ine.rest.did.receiver.UpdateReceiver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val productRepository: ProductRepository,
    private val dataStoreRepository: DataStoreRepository,
) : ViewModel() {
    private val TAG = this.javaClass.simpleName
    private val _uiState = MutableSharedFlow<UiState>()
    val uiState = _uiState.asSharedFlow()

    private val _productList = MutableStateFlow<List<Product>>(emptyList())
    private val _filteredProducts = MutableStateFlow<List<Product>>(emptyList())
    val productList: StateFlow<List<Product>> = _filteredProducts

    var device by mutableStateOf(Device())
    var displayCd: String? = null
    var rollingYn: String = "N"
    private val _currentTime = MutableStateFlow(getCurrentTime())

    init {
        uiState.onEach { Log.d(TAG, "uiState=$it") }.launchIn(viewModelScope)
        startTimer()

        viewModelScope.launch {
            val deviceId =dataStoreRepository.getDeviceId().first()
            val cmpCd = dataStoreRepository.getCmpCd().first()
            val salesOrgCd = dataStoreRepository.getSalesOrgCd().first()
            val storCd = dataStoreRepository.getStorCd().first()
            val cornerCd = dataStoreRepository.getCornerCd().first()

//            Log.d(TAG,"### 상품 화면 deviceId:$deviceId $cmpCd $salesOrgCd $storCd $cornerCd")
            device = deviceRepository.getDevice(deviceId).first()
            Log.d(TAG,"### 상품 화면 device:$device")
            displayCd = device.displayMenuCd!!
            rollingYn = device.rollingYn!!

            productRepository.getProductList(
                cmpCd, salesOrgCd, storCd, cornerCd
            ).collect { list ->
                _productList.value = list
                Log.d(TAG,"상품 목록:${list}")
                checkProductSale()
            }
        }
    }

    private fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)  // 0 ~ 23
        val minute = calendar.get(Calendar.MINUTE)     // 0 ~ 59

        return String.format("%02d%02d", hour, minute)  // "HHmm" 형식 문자열 반환
    }

    private fun startTimer() {
        viewModelScope.launch {
            delay(60 * 1000L)
            while (isActive) {
                _currentTime.value = getCurrentTime()

                checkProductSale()

                delay(60 * 1000L)
            }
        }
    }

    private fun checkProductSale() {
        Log.d(TAG,"상품 표시 체크")
        val now = getCurrentTime()
        Log.d(TAG, "현재 시간: $now")

        val todayIndex = getTodayIndex()

        _filteredProducts.value = _productList.value.filter { product ->
            val isToday = product.weekDiv[todayIndex] == '1'
            val inTimeRange = isInSaleTime(now, product.saleCloseStartTime, product.saleCloseEndTime)
            isToday && inTimeRange
        }
//        Log.d(TAG,"_filteredProducts:${_filteredProducts.value }")
    }

    private fun getTodayIndex(): Int {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1 // 0=일
    }

    private fun isInSaleTime(now: String, start: String?, end: String?): Boolean {
        if (start.isNullOrBlank() || end.isNullOrBlank()) return true
        return if (start <= end) {
            now < start || now > end
        } else {
            now > end && now < start
        }
    }
    fun updateSoldoutYn(data: String) {
        Log.d(TAG,"updateSoldoutYn data:$data")
        viewModelScope.launch(Dispatchers.IO) {
            val jsonObject = JSONObject(data)
            val cmpCd = jsonObject.getString("cmpCd")
            val salesOrgCd = jsonObject.getString("salesOrgCd")
            val storCd = jsonObject.getString("storCd")
            val itemCd = jsonObject.getString("itemCd")
            val soldoutYn = jsonObject.getString("soldoutYn")
            Log.d("TAG", "cmpCd: $cmpCd, itemCd: $itemCd, soldoutYn: $soldoutYn")
            productRepository.updateSoldoutYn(
                cmpCd, salesOrgCd, storCd, itemCd, soldoutYn
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