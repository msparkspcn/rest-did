package com.secta9ine.rest.did.presentation.order

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.secta9ine.rest.did.domain.model.OrderStatus
import com.secta9ine.rest.did.domain.repository.DataStoreRepository
import com.secta9ine.rest.did.domain.repository.DeviceRepository
import com.secta9ine.rest.did.domain.repository.OrderStatusRepository
import com.secta9ine.rest.did.network.WebSocketViewModel
import com.secta9ine.rest.did.util.SoldOutUpdater
import com.secta9ine.rest.did.util.VersionUpdater
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
    private val orderStatusRepository: OrderStatusRepository,
    private val soldOutUpdater: SoldOutUpdater,
    private val versionUpdater: VersionUpdater,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val TAG = this.javaClass.simpleName
    private val _uiState = MutableSharedFlow<UiState>()
    val uiState = _uiState.asSharedFlow()
    var cmpCd by mutableStateOf("")
    var salesOrgCd by mutableStateOf("")
    var storCd by mutableStateOf("")
    var cornerCd by mutableStateOf("")
    private var jobInit: Job
    var oriOrderList by mutableStateOf(emptyList<OrderStatus?>())
    var completedOrderList by mutableStateOf(emptyList<OrderStatus?>())

    var waitingOrderList: List<String> =
        listOf(
            "2134", "2135", "2136",
            "2137", "2138", "2139",
            "2140", "2141", "2142",
            "2145", "2146", "2147",
            "2148", "2149", "2150"
        )

    var currentCalledOrder: OrderStatus? =
    OrderStatus(
        seq = 1,
        saleDt = "20250703",
        cmpCd = "001",
        salesOrgCd = "100",
        storCd = "A01",
        cornerCd = "C1",
        orderNo = "21455",
        orderStatus = "C")
    private set

    var callOrderNo: String?
        private set

    init {
        callOrderNo = ""
        jobInit = viewModelScope.launch {

            cmpCd = dataStoreRepository.getCmpCd().first()
//            cmpCd = "SLKR"
            salesOrgCd = dataStoreRepository.getSalesOrgCd().first()
//            salesOrgCd = "8000"
            storCd = dataStoreRepository.getStorCd().first()
//            storCd = "5000511"
            cornerCd = dataStoreRepository.getCornerCd().first()
            Log.d(TAG,"11cmpCd:$cmpCd, salesOrgCd:$salesOrgCd, storCd:$storCd" +
                    ", cornerCd:$cornerCd")

            orderStatusRepository.get(
                saleDt = "20250707",
                cmpCd = cmpCd,
                salesOrgCd = salesOrgCd,
                storCd = storCd,
                cornerCd = cornerCd
            ).first().let {
                oriOrderList = it
                completedOrderList = it.filter { order -> order!!.orderStatus == "2" }
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
                val apkUrl = "http://o2pos.spcnetworks.kr/files/app/o2pos/download/backup/1123.apk"

                viewModelScope.launch {
                    versionUpdater.download(apkUrl) { downloadedFile ->
                        versionUpdater.installApk(context, downloadedFile)}
                }
            }
            is WebSocketViewModel.UiState.InsertOrder -> {
                //insert 처리(state:C DID호출)


            }

            else -> Unit // 다른 이벤트는 내가 처리하지 않음
        }
    }

    private fun scheduleStateUpdateToReady(order: OrderStatus) {
        CoroutineScope(Dispatchers.IO).launch {
            delay(60_000) // 1분

            // 상태가 여전히 C이면 2로 변경
            orderStatusRepository.getByOrderNoC(
                order.saleDt, order.cmpCd, order.salesOrgCd, order.storCd, order.cornerCd, order.orderNoC
            ).first().let {
                currentCalledOrder = it
            }

            if (currentCalledOrder?.orderStatus == "C") {
                orderStatusRepository.updateOrderStatus(
                    order.saleDt, order.cmpCd, order.salesOrgCd, order.storCd, order.cornerCd, order.orderNo,
                    "2")
//                scheduleStateUpdateToFinal(current.copy(orderStatus = "2"))
            }
        }
    }

//    private fun scheduleStateUpdateToFinal(order: OrderStatus) {
//        CoroutineScope(Dispatchers.IO).launch {
//            delay(5 * 60_000) // 5분
//
//            // 상태가 여전히 2이면 3으로 변경
//            val current = orderDao.getByOrderNo(order.orderNo)
//            if (current?.orderStatus == "2") {
//                orderDao.updateStatus(order.orderNo, "3")
//            }
//        }
//    }

    fun onCallOrder(orderNo: String) {
        callOrderNo = orderNo
    }

    fun onEnterKeyPressed() {
        Log.d(TAG, "환경 설정 화면 이동")
        viewModelScope.launch {
            _uiState.emit(UiState.NavigateToDevice)
        }
    }

    suspend fun getDisplayCd(): String {
        val device = deviceRepository.getDevice(
            dataStoreRepository.getDeviceId().first()
        ).firstOrNull() ?: throw RuntimeException("")
        val displayCd = device.displayMenuCd
        if (displayCd == "1234") {
            return "1234"
        }
        return ""
    }

//    suspend fun getSaleOpen(): SaleOpen {
//        viewModelScope.launch {
//
//        }
//    }


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