package com.secta9ine.rest.did.presentation.product

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.secta9ine.rest.did.domain.model.Device
import com.secta9ine.rest.did.domain.model.Product
import com.secta9ine.rest.did.domain.repository.DataStoreRepository
import com.secta9ine.rest.did.domain.repository.DeviceRepository
import com.secta9ine.rest.did.domain.repository.ProductRepository
import com.secta9ine.rest.did.network.WebSocketViewModel
import com.secta9ine.rest.did.util.CommonUtils
import com.secta9ine.rest.did.util.SoldOutUpdater
import com.secta9ine.rest.did.util.VersionUpdater
import dagger.hilt.android.lifecycle.HiltViewModel
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
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val productRepository: ProductRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val soldOutUpdater: SoldOutUpdater,
    private val versionUpdater: VersionUpdater
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

    fun handleSocketEvent(state: WebSocketViewModel.UiState) {
        when (state) {
            is WebSocketViewModel.UiState.UpdateDevice -> {
//                updateUiState(UiState.UpdateDevice)

            }
            is WebSocketViewModel.UiState.SoldOut -> {
                Log.d(TAG,"품절발생!!")
                viewModelScope.launch {
                    soldOutUpdater.update(state.data)
                }
            }
            is WebSocketViewModel.UiState.UpdateVersion -> {
                Log.d(TAG,"버전 업데이트")
            }
            else -> Unit // 다른 이벤트는 내가 처리하지 않음
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

    fun updateVersion(context: Context) {
        //ws으로 버전정보(버전, url)을 받고 현재 버전과 비교
        viewModelScope.launch {
            val currentVersion = CommonUtils.getAppVersion(context)
            Log.d(TAG,"currentVersion:$currentVersion")
        }
    }
    fun onEnterKeyPressed(context: Context) {
        Log.d(TAG,"Enter Key Pressed 22")
        viewModelScope.launch {

            isSystemApp(context)
            //api로 버전 정보 수신 후 다운로드 로직 수행
            val apkUrl = "http://o2pos.spcnetworks.kr/files/app/o2pos/download/backup/1123.apk"

//            downloadApkToExternal(context,apkUrl) { downloadedFile ->
//                installApk(context, downloadedFile)}

            versionUpdater.download(apkUrl) { downloadedFile ->
                versionUpdater.installApk(context, downloadedFile)}
//            _uiState.emit(UiState.NavigateToDevice)
        }
    }

    suspend fun getDisplayCd(): String {
        val device = deviceRepository.getDevice(
            dataStoreRepository.getDeviceId().first()
        ).firstOrNull() ?: throw RuntimeException("")
        return device.displayMenuCd!!

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
        object UpdateVersion : UiState
        object NavigateToDevice : UiState
        object NavigateToOrderStatus :UiState
        object Idle : UiState
        data class Error(val message: String) : UiState
    }
}