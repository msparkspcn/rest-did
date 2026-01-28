package com.secta9ine.rest.did.presentation.product

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.secta9ine.rest.did.domain.model.Device
import com.secta9ine.rest.did.domain.model.ProductVo
import com.secta9ine.rest.did.domain.repository.DataStoreRepository
import com.secta9ine.rest.did.domain.repository.DeviceRepository
import com.secta9ine.rest.did.domain.repository.ProductRepository
import com.secta9ine.rest.did.network.WebSocketViewModel
import com.secta9ine.rest.did.util.SoldOutUpdater
import com.secta9ine.rest.did.util.UiString
import com.secta9ine.rest.did.util.VersionUpdater
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
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
    private val versionUpdater: VersionUpdater,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val tag = this.javaClass.simpleName

    private val _state = MutableStateFlow(ProductScreenState())
    val state: StateFlow<ProductScreenState> = _state

    private val _uiState = MutableSharedFlow<UiState>()
    val uiState = _uiState.asSharedFlow()

    companion object {
        private const val ROLLING_DELAY = 5000L
        private const val PAGE_SIZE = 12
        private const val UPDATE_APK_URL = "http://o2pos.spcnetworks.kr/files/app/o2pos/download/backup/1123.apk"
    }

    init {
        startTimer()
        loadInitialData()
        startRolling()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                val deviceId = dataStoreRepository.getDeviceId().first()
                val displayMenuCd = dataStoreRepository.getDisplayMenuCd().first()
                val cmpCd = dataStoreRepository.getCmpCd().first()
                val salesOrgCd = dataStoreRepository.getSalesOrgCd().first()
                val storCd = dataStoreRepository.getStorCd().first()
                val cornerCd = dataStoreRepository.getCornerCd().first()
                val displayCornerCds = dataStoreRepository.getDisplayCorners().first()

                val corners: Set<String> = if (displayMenuCd == "06") {
                    displayCornerCds
                } else {
                    setOf(cornerCd)
                }

                val device = deviceRepository.getDevice(deviceId).first()
                _state.value = _state.value.copy(device = device)

                productRepository.getProductList(cmpCd, salesOrgCd, storCd, corners).collect { list ->
                    _state.value = _state.value.copy(productList = list)
                    updateFilteredProducts()
                }
            } catch (e: Exception) {
                Log.e(tag, "Failed to load initial data", e)
            }
        }
    }

    private fun startTimer() {
        viewModelScope.launch {
            while (isActive) {
                val now = getCurrentTime()
                if (now != _state.value.currentTime) {
                    _state.value = _state.value.copy(currentTime = now)
                    updateFilteredProducts()
                }
                delay(1000L) // Check every second for better precision
            }
        }
    }

    private fun startRolling() {
        viewModelScope.launch {
            var currentIndex = 0
            while (isActive) {
                val filtered = _state.value.filteredProducts
                val device = _state.value.device
                val rollingYn = device.rollingYn ?: "N"
                val displayCd = device.displayMenuCd ?: ""

                val pageSize = when (displayCd) {
                    "01" -> 1
                    "03" -> 2
                    "04" -> 8
                    "05" -> 3
                    "07" -> 12
                    else -> 12
                }

                if (filtered.isEmpty()) {
                    _state.value = _state.value.copy(displayedProducts = emptyList())
                    delay(ROLLING_DELAY)
                    continue
                }

                if (filtered.size <= pageSize || rollingYn == "N") {
                    _state.value = _state.value.copy(displayedProducts = filtered.take(pageSize))
                    delay(ROLLING_DELAY)
                    currentIndex = 0
                } else {
                    val endIndex = minOf(currentIndex + pageSize, filtered.size)
                    _state.value = _state.value.copy(displayedProducts = filtered.subList(currentIndex, endIndex))
                    
                    delay(ROLLING_DELAY)
                    
                    currentIndex = if (endIndex >= filtered.size) 0 else endIndex
                }
            }
        }
    }

    private fun updateFilteredProducts() {
        val now = _state.value.currentTime
        val todayIndex = getTodayIndex()

        val filtered = _state.value.productList.filter { product ->
            val isToday = product.weekDiv.getOrNull(todayIndex) == '1'
            val inTimeRange = isInSaleTime(now, product.saleCloseStartTime, product.saleCloseEndTime)
            isToday && inTimeRange
        }
        _state.value = _state.value.copy(filteredProducts = filtered, isLoading = false)
    }

    private fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        return String.format("%02d%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
    }

    private fun getTodayIndex(): Int {
        return (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1) // 0=Sun
    }

    private fun isInSaleTime(now: String, start: String?, end: String?): Boolean {
        if (start.isNullOrBlank() || end.isNullOrBlank()) return true
        return if (start <= end) {
            now < start || now > end
        } else {
            now > end && now < start
        }
    }

    fun handleSocketEvent(event: WebSocketViewModel.UiState) {
        when (event) {
            is WebSocketViewModel.UiState.SoldOut -> {
                viewModelScope.launch {
                    soldOutUpdater.update(event.data)
                }
            }
            is WebSocketViewModel.UiState.UpdateVersion -> {
                viewModelScope.launch {
                    versionUpdater.download(UPDATE_APK_URL) { downloadedFile ->
                        versionUpdater.installApk(context, downloadedFile)
                    }
                }
            }
            else -> Unit
        }
    }

    fun onEnterKeyPressed() {
        viewModelScope.launch {
            versionUpdater.download(UPDATE_APK_URL) { downloadedFile ->
                versionUpdater.installApk(context, downloadedFile)
            }
        }
    }

    data class ProductScreenState(
        val isLoading: Boolean = true,
        val device: Device = Device(),
        val productList: List<ProductVo> = emptyList(),
        val filteredProducts: List<ProductVo> = emptyList(),
        val displayedProducts: List<ProductVo> = emptyList(),
        val currentTime: String = "",
        val errorMessage: UiString? = null
    )

    sealed interface UiState {
        object Loading : UiState
        object UpdateDevice : UiState
        object UpdateVersion : UiState
        object NavigateToDevice : UiState
        object NavigateToOrderStatus : UiState
        object Idle : UiState
        data class Error(val message: String) : UiState
    }
}