package com.secta9ine.rest.did.presentation.product

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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.lang.RuntimeException
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
    init {
        uiState.onEach { Log.d(TAG, "uiState=$it") }.launchIn(viewModelScope)


        viewModelScope.launch {
            val deviceId =dataStoreRepository.getDeviceId().first()
            device = deviceRepository.getDevice(deviceId).first()

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

    fun onEnterKeyPressed() {
        Log.d(TAG,"장비설정화면 이동")
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

    sealed interface UiState {
        object Loading : UiState
        object UpdateDevice : UiState
        object NavigateToDevice : UiState
        object NavigateToOrderStatus :UiState
        object Idle : UiState
        data class Error(val message: String) : UiState
    }
}