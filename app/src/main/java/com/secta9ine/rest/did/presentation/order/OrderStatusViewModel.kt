package com.secta9ine.rest.did.presentation.order

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.secta9ine.rest.did.domain.model.OrderStatus
import com.secta9ine.rest.did.domain.model.SaleOpen
import com.secta9ine.rest.did.domain.repository.DataStoreRepository
import com.secta9ine.rest.did.domain.repository.DeviceRepository
import com.secta9ine.rest.did.domain.repository.OrderStatusRepository
import com.secta9ine.rest.did.domain.repository.RestApiRepository
import com.secta9ine.rest.did.domain.repository.SaleOpenRepository
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.Collections.emptyList
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class OrderStatusViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val deviceRepository: DeviceRepository,
    private val orderStatusRepository: OrderStatusRepository,
    private val soldOutUpdater: SoldOutUpdater,
    private val versionUpdater: VersionUpdater,
    private val restApiRepository: RestApiRepository,
    private val saleOpenRepository: SaleOpenRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val tag = this.javaClass.simpleName
    private val _uiState = MutableSharedFlow<UiState>()
    val uiState = _uiState.asSharedFlow()

    val cmpCd = MutableStateFlow("")
    val salesOrgCd = MutableStateFlow("")
    val storCd = MutableStateFlow("")
    val cornerCd = MutableStateFlow("")

    private var jobInit: Job

    private val _oriOrderList = MutableStateFlow<List<OrderStatus>>(emptyList())
    private val oriOrderList: StateFlow<List<OrderStatus>> = _oriOrderList.asStateFlow()

    private val completedOrderList: StateFlow<List<OrderStatus>> = _oriOrderList.map { list ->
        list.filter { it.status == "4" }
            .sortedBy { it.updDate } //
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    private val waitingOrderList: StateFlow<List<OrderStatus>> = _oriOrderList.map { list ->
        list.filter { it.status == "2" }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    private val _currentCalledOrder = MutableStateFlow<OrderStatus?>(null)
    val currentCalledOrder: StateFlow<OrderStatus?> = _currentCalledOrder.asStateFlow()

    private val _saleOpen = MutableStateFlow<SaleOpen?>(null)

    private val _displayedCompletedOrders = MutableStateFlow<List<OrderStatus>>(emptyList())
    val displayedCompletedOrders: StateFlow<List<OrderStatus>> = _displayedCompletedOrders.asStateFlow()

    private val _displayedWaitingOrders = MutableStateFlow<List<OrderStatus>>(emptyList())
    val displayedWaitingOrders: StateFlow<List<OrderStatus>> = _displayedWaitingOrders.asStateFlow()

    private var completedIndex = 0
    private var waitingIndex = 0
    private var completedJob: Job? = null
    private var waitingJob: Job? = null
    private val completedTimers = mutableMapOf<String, Job>()

    init {
        uiState.onEach { Log.d(tag, "uiState=$it") }.launchIn(viewModelScope)
        jobInit = viewModelScope.launch {
            _uiState.emit(UiState.Loading)

            cmpCd.value = dataStoreRepository.getCmpCd().first()
            salesOrgCd.value = dataStoreRepository.getSalesOrgCd().first()
            storCd.value = dataStoreRepository.getStorCd().first()
            cornerCd.value = dataStoreRepository.getCornerCd().first()
            Log.d(tag,"11cmpCd:$cmpCd, salesOrgCd:$salesOrgCd, storCd:$storCd" +
                    ", cornerCd:$cornerCd")

            val result = restApiRepository.getSaleOpen(
                cmpCd = cmpCd.value,
                salesOrgCd = salesOrgCd.value,
                storCd = storCd.value
            )

            //개점 정보 있으면 insert 후 주문 목록 조회
            if (result.data != null) {
                _saleOpen.value = result.data
                Log.d(tag, "saleOpen:${_saleOpen.value}, cornerCd:${cornerCd.value}")
                _saleOpen.value?.let { saleOpenRepository.insert(it) }
                restApiRepository.getOrderList(
                    cmpCd = cmpCd.value,
                    salesOrgCd = salesOrgCd.value,
                    storCd = storCd.value,
                    cornerCd = cornerCd.value,
                    saleDt = _saleOpen.value!!.saleDt
                ).let { result1 ->
                    result1.data?.filterNotNull()?.let {orderList ->
                        Log.d(tag,"주문 목록:$orderList")
                        orderStatusRepository.insertAll(orderList)
                    }
                }
            } else { //없으면 로컬에서 개점 정보 조회
                Log.e(tag, "saleOpen 데이터 null")
                _saleOpen.value = saleOpenRepository.get(cmpCd.value, salesOrgCd.value, storCd.value).first()
                Log.d(tag,"saleOpen:${_saleOpen.value}")
                if(_saleOpen.value ==null) {    //로컬에 개점 정보가 없으면 주문 정보도 가져오지 못함
                    _uiState.emit(UiState.Error("개점 정보가 없습니다."))
                    return@launch
                }
            }

            _saleOpen.value?.let { currentSaleOpen ->
                orderStatusRepository.get(
                    saleDt = currentSaleOpen.saleDt,
                    cmpCd = cmpCd.value,
                    salesOrgCd = salesOrgCd.value,
                    storCd = storCd.value,
                    cornerCd = cornerCd.value
                ).collectLatest {list ->
                    try {
                        _oriOrderList.value = list
//                            Log.d(tag, "oriOrderList:${_oriOrderList.value}")

                        _currentCalledOrder.value = _oriOrderList.value.find { it.status == "C" }
                        _currentCalledOrder.value?.let { it1 -> scheduleStateUpdateToReady(it1) }
//                            Log.d(tag,"oriOrderList:$oriOrderList")
                        Log.d(tag,"currentCalledOrder:${_currentCalledOrder.value}")

//                            Log.d(tag,"completedOrderList:$completedOrderList")
                        updateDisplayedLists()
                        scheduleStateUpdateToFinal()
                        _uiState.emit(UiState.Idle)
                        startRolling()
                    } catch (e: Exception) {
                        Log.e(tag, "주문 처리 중 예외 발생", e)
                        _uiState.emit(UiState.Error("주문 처리 실패: ${e.message}"))
                    }
                }
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
        val chunked = completedOrderList.value.chunked(6)
        _displayedCompletedOrders.value = chunked.getOrNull(completedIndex).orEmpty()
    }

    private fun updateWaitingSlice() {
        val chunked = waitingOrderList.value.chunked(9)
        _displayedWaitingOrders.value = chunked.getOrNull(waitingIndex).orEmpty()
    }

    private fun startRolling() {
        completedJob?.cancel()
        waitingJob?.cancel()

        completedJob = viewModelScope.launch {
            completedOrderList.collectLatest { list ->
                if (list.size < 6) {
                    Log.d(tag, "completedOrderList size:${list.size}")
                    _displayedCompletedOrders.value = list // 6개 미만이면 전체 표시
                    completedIndex = 0 // 인덱스 초기화
                } else {
                    _displayedCompletedOrders.value = list.chunked(6).getOrNull(completedIndex).orEmpty()
                    while (isActive) { // 코루틴이 활성 상태일 때만 반복
                        delay(5000)
                        Log.d(tag, "완료 주문 롤링")
                        val totalPages = list.chunked(6).size
                        completedIndex = (completedIndex + 1) % totalPages.coerceAtLeast(1)
                        _displayedCompletedOrders.value = list.chunked(6).getOrNull(completedIndex).orEmpty()
                    }
                }
            }
        }

        waitingJob = viewModelScope.launch {
            waitingOrderList.collectLatest { list ->
                if (list.size < 9) {
                    Log.d(tag, "waitingOrderList size:${list.size}")
                    _displayedWaitingOrders.value = list // 9개 미만이면 전체 표시
                    waitingIndex = 0 // 인덱스 초기화
                } else {
                    _displayedWaitingOrders.value = list.chunked(9).getOrNull(waitingIndex).orEmpty()
                    while (isActive) {
                        delay(5000)
                        Log.d(tag, "대기 주문 롤링")
                        val totalPages = list.chunked(9).size
                        waitingIndex = (waitingIndex + 1) % totalPages.coerceAtLeast(1)
                        _displayedWaitingOrders.value = list.chunked(9).getOrNull(waitingIndex).orEmpty()
                    }
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
                Log.d(tag,"품절 발생!!")
                viewModelScope.launch {
                    soldOutUpdater.update(state.data)
                }
            }
            is WebSocketViewModel.UiState.UpdateVersion -> {
                Log.d(tag,"버전 업데이트")
                val apkUrl = "http://o2pos.spcnetworks.kr/files/app/o2pos/download/backup/1123.apk"

                viewModelScope.launch {
                    versionUpdater.download(apkUrl) { downloadedFile ->
                        versionUpdater.installApk(context, downloadedFile)}
                }
            }
            is WebSocketViewModel.UiState.InsertOrder -> {
                Log.d(tag,"주문 발생")
                viewModelScope.launch {
                    createOrder(state.data)
                }
            }
            is WebSocketViewModel.UiState.InsertSaleOpen -> {
                Log.d(tag,"개점 발생")
                viewModelScope.launch {
                    createSaleOpen(state.data)
                }
            }
            else -> Unit // 다른 이벤트는 내가 처리하지 않음
        }
    }

    private fun scheduleStateUpdateToReady(order: OrderStatus) {
        CoroutineScope(Dispatchers.IO).launch {
            delay(20_000)
            Log.d(tag,"20초 경과")
            // 상태가 여전히 C이면 2로 변경
            val refreshedOrder = orderStatusRepository.getByOrderNoC(
                order.saleDt, order.cmpCd, order.salesOrgCd, order.storCd, order.cornerCd, order.orderNoC
            ).first()

            if (refreshedOrder?.status == "C") {
                Log.d(tag,"호출 있음")
                orderStatusRepository.updateOrderStatus(
                    order.saleDt, order.cmpCd, order.salesOrgCd, order.storCd, order.cornerCd, order.tradeNo,
                    order.posNo, "4")
            }
        }
    }

    private fun scheduleStateUpdateToFinal() {
        viewModelScope.launch {
            completedOrderList.collectLatest { list ->
                val trackedTradeNos = list.map { it.tradeNo }.toSet()

                // 새로 들어온 주문에만 타이머 설정
                list.forEach { order ->
                    val tradeNo = order.tradeNo
                    if (!completedTimers.containsKey(tradeNo)) {
                        val job = launch {
                            try {
                                Log.d(tag, "타이머 시작: ${order.tradeNo}")
                                delay(60_000) // 60초 후 상태 변경

                                Log.d(tag, "60초 경과 후 상태 '5'로 변경: ${order.tradeNo}")
                                withContext(Dispatchers.IO) {
                                    orderStatusRepository.updateOrderStatus(
                                        saleDt = order.saleDt,
                                        cmpCd = order.cmpCd,
                                        salesOrgCd = order.salesOrgCd,
                                        storCd = order.storCd,
                                        cornerCd = order.cornerCd,
                                        tradeNo = order.tradeNo,
                                        posNo = order.posNo,
                                        status = "5"
                                    )
                                }
                            } catch (e: CancellationException) {
                                Log.d(tag, "타이머 취소됨: $tradeNo")
                            } finally {
                                completedTimers.remove(tradeNo)
                            }
                        }
                        completedTimers[tradeNo] = job
                    }
                }

                // 이미 사라진 주문은 타이머 취소
                val toRemove = completedTimers.keys - trackedTradeNos
                toRemove.forEach { tradeNo ->
                    completedTimers[tradeNo]?.cancel()
                    completedTimers.remove(tradeNo)
                }
            }
        }
    }
    override fun onCleared() {
        super.onCleared()
        completedJob?.cancel()
//        waitingJob?.cancel()
    }

    fun onEnterKeyPressed() {
        Log.d(tag, "환경 설정 화면 이동")
        viewModelScope.launch {
            _uiState.emit(UiState.NavigateToDevice)
        }
    }

    private suspend fun createSaleOpen(data: String) {
        try {
            val saleOpen = JSONObject(data)
//            saleOpenRepository.insert(saleOpen)
            //자동으로 개점일 변경되는지 확인
            //개점일 변경 후 주문 수신 다시

        }
        catch(e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun createOrder(data: String) {
        try {
            val order = JSONObject(data)
            val saleDt = order.optString("saleDt", "")
            val cmpCd = order.optString("cmpCd", "")
            val salesOrgCd = order.optString("salesOrgCd", "")
            val storCd = order.optString("storCd", "")
            val posNo = order.optString("posNo", "")
            val tradeNo = order.optString("tradeNo", "")
            val cornerCd = order.optString("cornerCd", "")
            val updDate = System.currentTimeMillis() / 1000

            val ordTime = order.optString("ordTime").takeIf { it.isNotEmpty() }
            val comTime = order.optString("updDate").takeIf { it.isNotEmpty() }
            val orderNoC = order.optString("orderNoC", "")
            val status = order.optString("status", "")
            orderStatusRepository.updateOrderCallStatus(
                saleDt = saleDt,
                cmpCd = cmpCd,
                salesOrgCd = salesOrgCd,
                storCd = storCd,
                cornerCd = cornerCd
            )
            val orderStatusCnt = orderStatusRepository.getCnt(
                saleDt = saleDt,
                cmpCd = cmpCd,
                salesOrgCd = salesOrgCd,
                storCd = storCd,
                cornerCd = cornerCd,
                posNo = posNo,
                tradeNo = tradeNo
            )
            Log.d(tag,"orderStatusCnt:$orderStatusCnt")
            if(orderStatusCnt > 0) {
                orderStatusRepository.updateOrderStatus(
                    saleDt = saleDt,
                    cmpCd = cmpCd,
                    salesOrgCd = salesOrgCd,
                    storCd = storCd,
                    cornerCd = cornerCd,
                    posNo = posNo,
                    tradeNo = tradeNo,
                    status = when (status) {
                        "2" -> "2"
                        "4" -> "C"
                        "5" -> "5"
                        else -> "E"
                    },
                )
            }
            else {
                orderStatusRepository.insert(OrderStatus(
                    saleDt = saleDt,
                    cmpCd = cmpCd,
                    salesOrgCd = salesOrgCd,
                    storCd = storCd,
                    cornerCd = cornerCd,
                    posNo = posNo,
                    tradeNo = tradeNo,
                    status = when (status) {
                        "2" -> "2"
                        "4" -> "C"
                        "5" -> "5"
                        else -> "E"
                    },
                    ordTime = ordTime,
                    comTime = comTime,
                    orderNoC = orderNoC,
                    updUserId = null,
                    updDate = updDate
                ))
            }
        } catch (e: Exception) {
            e.printStackTrace()
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