package com.secta9ine.rest.did.presentation.order

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.secta9ine.rest.did.domain.repository.DataStoreRepository
import com.secta9ine.rest.did.domain.repository.DeviceRepository
import com.secta9ine.rest.did.presentation.splash.SplashViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.lang.RuntimeException
import javax.inject.Inject

@HiltViewModel
class OrderStatusViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val deviceRepository: DeviceRepository,
) : ViewModel() {
    private val TAG = this.javaClass.simpleName
    private val _uiState = MutableSharedFlow<UiState>()
    val uiState = _uiState.asSharedFlow()

    var completedOrderList: List<String> =
        listOf(
            "1149", "1148", "1147",
            "1146", "1145", "1142",
            "1141", "1140", "1139",
            "1138", "1137", "1136",
            "1135", "1134", "1133"
        )
        private set

    var waitingOrderList: List<String> =
        listOf(
            "2134", "2135", "2136",
            "2137", "2138", "2139",
            "2140", "2141", "2142",
            "2145", "2146", "2147",
            "2148", "2149", "2150"
        )

    var callOrderNo: String?
    private set

    init {
        callOrderNo =""
    }

    fun onCallOrder(orderNo : String) {
        callOrderNo = orderNo
    }

    fun onEnterKeyPressed() {
        Log.d(TAG,"환경 설정 화면 이동")
        viewModelScope.launch {
            _uiState.emit(UiState.NavigateToDevice)
        }
    }

    suspend fun getDisplayCd(): String {
        val device = deviceRepository.getDevice(
            dataStoreRepository.getDeviceId().first()
        ).firstOrNull() ?: throw RuntimeException("")
        val displayCd = device.displayMenuCd
        if(displayCd == "1234") {
            return "1234"
        }
        return ""
    }

    suspend fun updateUiState(state: UiState) {
        _uiState.emit(state)
    }

    sealed interface UiState {
        object Loading : UiState
        object UpdateDevice : UiState
        object NavigateToDevice : UiState
        object NavigateToProduct : UiState
        object Idle : UiState
        data class Error(val message: String) : UiState

    }
}