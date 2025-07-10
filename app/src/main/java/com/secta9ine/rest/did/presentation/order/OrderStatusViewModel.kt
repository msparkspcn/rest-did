package com.secta9ine.rest.did.presentation.order

import android.content.Context
import android.util.Log
import androidx.compose.runtime.derivedStateOf
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

    val completedOrderList by derivedStateOf {
        oriOrderList.filterNotNull().filter { it?.orderStatus == "4" }
    }

    val waitingOrderList by derivedStateOf {
        oriOrderList.filterNotNull().filter { it?.orderStatus == "2" }
    }

    val currentCalledOrder by derivedStateOf {
        oriOrderList.find { it?.orderStatus == "C" }
    }

    var displayedCompletedOrders by mutableStateOf<List<OrderStatus>>(emptyList())
        private set
    var displayedWaitingOrders by mutableStateOf<List<OrderStatus>>(emptyList())
        private set
    private var completedIndex = 0
    private var waitingIndex = 0
    private var completedJob: Job? = null
    private var waitingJob: Job? = null

    var callOrderNo: String?
        private set

    init {
        callOrderNo = ""
        jobInit = viewModelScope.launch {
            _uiState.emit(UiState.Loading)
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
                currentCalledOrder?.let { it1 -> scheduleStateUpdateToReady(it1) }
                Log.d(TAG,"oriOrderList:$oriOrderList")
                Log.d(TAG,"completedOrderList:$completedOrderList")
                updateDisplayedLists()
                startRolling()
                _uiState.emit(UiState.Idle)
            }
        }
    }

    private fun updateDisplayedLists() {
        completedIndex = 0
        waitingIndex = 0
        updateCompletedSlice()
        updateWaitingSlice()
    }

    private fun updateCompletedSlice() {
        val chunked = completedOrderList.chunked(6)
        displayedCompletedOrders = chunked.getOrNull(completedIndex).orEmpty()
    }

    private fun updateWaitingSlice() {
        val chunked = waitingOrderList.chunked(9)
        displayedWaitingOrders = chunked.getOrNull(waitingIndex).orEmpty()
    }

    private fun startRolling() {
        completedJob?.cancel()
        waitingJob?.cancel()

        completedJob = viewModelScope.launch {
            if(completedOrderList.size < 6) {
                Log.d(TAG,"size:${completedOrderList.size}")
            }
            else {
                while (true) {
                    delay(5000)
                    Log.d(TAG,"롤링")
                    val total = completedOrderList.chunked(6).size
                    completedIndex = (completedIndex + 1) % total.coerceAtLeast(1)
                    updateCompletedSlice()
                }
            }
        }

        waitingJob = viewModelScope.launch {
            if(waitingOrderList.size < 9) {
                Log.d(TAG,"waitingOrderList size:${waitingOrderList.size}")
            }
            else {
                while (true) {
                    delay(5000)
                    val total = waitingOrderList.chunked(9).size
                    waitingIndex = (waitingIndex + 1) % total.coerceAtLeast(1)
                    updateWaitingSlice()
                }
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
            delay(20_000)
            Log.d(TAG,"20초 경과")
            // 상태가 여전히 C이면 2로 변경
            val refreshedOrder = orderStatusRepository.getByOrderNoC(
                order.saleDt, order.cmpCd, order.salesOrgCd, order.storCd, order.cornerCd, order.orderNoC
            ).first()

            if (refreshedOrder?.orderStatus == "C") {
                Log.d(TAG,"호출 있음")
                orderStatusRepository.updateOrderStatus(
                    order.saleDt, order.cmpCd, order.salesOrgCd, order.storCd, order.cornerCd, order.orderNo,
                    "4")

                oriOrderList = orderStatusRepository.get(
                    saleDt = order.saleDt,
                    cmpCd = order.cmpCd,
                    salesOrgCd = order.salesOrgCd,
                    storCd = order.storCd,
                    cornerCd = order.cornerCd
                ).first()
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
    override fun onCleared() {
        super.onCleared()
        completedJob?.cancel()
//        waitingJob?.cancel()
    }
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