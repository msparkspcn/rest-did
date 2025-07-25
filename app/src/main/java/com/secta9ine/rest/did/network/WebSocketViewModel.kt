package com.secta9ine.rest.did.network

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.secta9ine.rest.did.MainActivity
import com.secta9ine.rest.did.domain.repository.DataStoreRepository
import com.secta9ine.rest.did.domain.usecase.RegisterUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject
import kotlin.system.exitProcess

@HiltViewModel
class WebSocketViewModel
@Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository,
    private val registerUseCases: RegisterUseCases,
) : AndroidViewModel(application) {
    private val tag = this.javaClass.simpleName
    private var webSocket: WebSocket? = null
    private var isConnected = false
    private var retryJob: Job? = null
    private var reconnectJob: Job? = null
    private val _uiState = MutableSharedFlow<UiState>()
    val uiState = _uiState.asSharedFlow()
    private val handlers = mutableSetOf<(UiState) -> Unit>()
    private val _androidId = MutableStateFlow("")
    private val _salesOrgCd = MutableStateFlow("")
    private val _storCd = MutableStateFlow("")
    private val _cornerCd = MutableStateFlow("")
    val androidId: StateFlow<String> = _androidId.asStateFlow()
    init {
        uiState.onEach { Log.d(tag, "uiState=$it") }.launchIn(viewModelScope)
        viewModelScope.launch {
            _salesOrgCd.value = dataStoreRepository.getSalesOrgCd().first()
            _storCd.value = dataStoreRepository.getStorCd().first()
            _cornerCd.value = dataStoreRepository.getCornerCd().first()
            Log.d(tag, "salesOrgCd: ${_salesOrgCd.value}, storCd: ${_storCd.value}, cornerCd: ${_cornerCd.value}")
            if(_salesOrgCd.value!=null && _storCd.value!=null && _cornerCd.value!=null) {
                observeNetworkChanges()
                connectWebSocket()
            }
            else {
                Log.d(tag,"not ready")
            }
            // 이후 로직 실행
        }


    }

    fun registerHandler(handler: (UiState) -> Unit) {
        handlers += handler
    }

    fun unregisterHandler(handler: (UiState) -> Unit) {
        handlers -= handler
    }

    suspend fun emitUiState(state: UiState) {
        _uiState.emit(state)
        handlers.forEach { it(state) }
    }

    private fun connectWebSocket() {
        Log.d(tag,"connectWebSocket")
        _androidId.value = Settings.Secure.getString(getApplication<Application>().contentResolver, Settings.Secure.ANDROID_ID)

        Log.d(tag, "ANDROID_ID: ${_androidId.value}")
        if (isConnected) {
            Log.d(tag,"already connected")
            return
        }

        WebSocketManager.connect(object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d(tag, "Connected") //connect 후 구독 요청
                this@WebSocketViewModel.webSocket = webSocket
                isConnected = true
                subscribeDeviceEvent() //장비 이벤트 구독(cmp,sales,stor,corner,deviceNo,displayMenuCd,rollingYn,apiKey 변경 여부 감지)
                subscribeSoldOutEvent()
                subscribeOrderEvents() //주문 이벤트 구독
//                subscribeRestartEvents() //재실행 이벤트 구독
                //DID 상품 이벤트 구독
                //DID 상품 부가 정보 이벤트 구독
                //주문 이벤트 구독
                subscribeToEvents()
                viewModelScope.launch {
                    emitUiState(UiState.CheckDevice)
                }
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    Log.d(tag, "Message received: $text")

                    /**/
                    val jsonObject =JSONObject(text)
                    val event = jsonObject.getString("type")
//                    val data = jsonObject.getString("body")
                    val data = jsonObject.optString("body", "")
                    val message = jsonObject.optString("message","")
                    val data2 = jsonObject.optString("data", "")
                    when (event) {
//                        "ECHO" -> {
//                            Log.d(tag, "앱 업데이트")
//                            viewModelScope.launch {
////                                emitUiState(UiState.UpdateDevice)
//                                emitUiState(UiState.UpdateVersion)
//                            }
////                            exitProcess(0)
//                        }
                        "ECHO" -> {
                            Log.d(tag, "앱 재실행11")
                            restartApp(getApplication())
                        }
                        "SOLDOUT" -> {
                            Log.d(tag, "상품 품절 이벤트 data:$data")
                            viewModelScope.launch {
                                emitUiState(UiState.SoldOut(data))
                            }
                        }
                        "order" -> {
                            Log.d(tag, "주문 이벤트 data:$data2")
                            viewModelScope.launch {
                                emitUiState(UiState.InsertOrder(data2))
                            }
                        }
                        "MASTER" -> {
                            Log.d(tag, "마스터 재수신")
                            //품절 외 마스터 정보 업데이트 됐을 때 재수신. 화면에는 상품 업데이트 중입니다. 등 띄워두기
                        }
                        "ACTIVE" -> {     //장비 활성화
                            Log.d(tag,"message:$message")
                            viewModelScope.launch {
                                setDeviceInfo(JSONObject(message))
                                _androidId.value = dataStoreRepository.getDeviceId().first()
                                Log.d(tag,"ws androidId:${_androidId.value}")
                                emitUiState(UiState.CheckDevice)

//                                restApiRepository.checkDevice(androidId).let {
//                                    when (it) {
//                                        is Resource.Success -> {
//                                            if (it.data != null) {  //인증된 경우 재등록 필요 없음.
//                                                var device = it.data
//                                                if (device.apiKey != null) {
//                                                    RestApiService.updateAuthToken(device.apiKey!!)
//                                                }
//                                                if (it.data != null) {
//                                                    registerUseCases.register(device)
//                                                    _uiState.emit(SplashViewModel.UiState.UpdateDevice)
//                                                } else {
//
//                                                }
//                                            } else {
//                                                restApiRepository.registerDeviceId(androidId).let {    //등록
//                                                    Log.d(TAG, "device info:${it}")
//                                                }
//                                            }
//                                        }
//
//                                        is Resource.Failure -> {}
//                                    }
//                                }

                                /*
                                registerUseCases.fetch(
                                    deviceId = androidId
                                ).let {
                                    when(it) {
                                        is Resource.Success -> {
                                            Log.d(tag,"ws fetch Success")
                                            registerUseCases.register(it.data!!)
                                            _uiState.emit(UiState.UpdateDevice)
                                        }
                                        is Resource.Failure -> {
                                            Log.d(tag,"ws fetch Failure")
                                        }
                                    }
                                }
                                 */
//                                navController.navigate(Screen.OrderStatusScreen.route)
                //                            _uiState.emit(UiState.UpdateDevice)
                            }
                        }

                        "order_updated" -> {
                            //주문상태 업데이트 처리(update)
                            val data = jsonObject.getJSONObject("data")
                        }
                        "product_updated" -> {
                            val data = jsonObject.getJSONObject("data")
                        }
                    }
                } catch (e: JSONException) {
                    Log.d(tag,"JSON Parsing Error:${e.message}")
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e(tag, "Error: ${t.message ?: "Unknown error"}")

                isConnected = false
                WebSocketManager.close()
                this@WebSocketViewModel.webSocket = null

                Log.d(tag, "Attempting to retry connection in 5 seconds...")
                retryWebSocket()
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.d(tag, "Closed: $reason")
                isConnected = false
                if(code != 1000) {
                    retryWebSocket() // 수동으로 종료되지 않았다면 재연결
                }
            }
        })
    }

    fun restartApp(context: Context) {
        Log.d(tag,"재시작~")
        val intent = Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // 1초 뒤 앱 재실행 예약
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + 1000,
            pendingIntent
        )

        // 실제 종료
        exitProcess(0)
    }


    private suspend fun setDeviceInfo(messageObject:JSONObject) {

        dataStoreRepository.setCmpCd(messageObject.getString("cmpCd"))
//                        dataStoreRepository.setSalesOrgCd(messageObject.getString("salesOrgCd"))
        dataStoreRepository.setStorCd(messageObject.getString("storCd"))
        dataStoreRepository.setCornerCd(messageObject.getString("cornerCd"))
        dataStoreRepository.setDeviceId(messageObject.getString("deviceId"))
        dataStoreRepository.setDeviceNo(messageObject.getString("deviceNo"))
//        RestApiService.updateAuthToken(messageObject.getString("apiKey"))
    }

    private fun sendMessage(message: String) {
        if (webSocket != null) {
            webSocket?.send(message)
        } else {
            Log.e("WebSocket", "WebSocket not connected. Trying to reconnect...")
            forceReconnect()
        }
    }

    override fun onCleared() {
        super.onCleared()
        WebSocketManager.close()
        retryJob?.cancel()
    }

    private fun retryWebSocket() {
        retryJob?.cancel() // 기존 재연결 시도 중이면 취소
        retryJob = viewModelScope.launch {
            delay(5000) // 5초 후 재연결 시도
            connectWebSocket()
        }
    }

    private fun subscribeDeviceEvent() {    //장비 이벤트 구독
        val subscribeMessage = """{"type": "subscribe", "topic":"DEVICE", "deviceId": "${_androidId.value}", "salesOrgCd":"${_salesOrgCd.value}", "storCd":"${_storCd.value}", "cornerCd":"${_cornerCd.value}", "deviceType":"DID"}"""
        sendMessage(subscribeMessage)
    }
    private fun subscribeSoldOutEvent() {    //품절 이벤트 구독
        val subscribeMessage = """{"type": "subscribe", "topic":"item", "deviceId": "${_androidId.value}", "salesOrgCd":"${_salesOrgCd.value}", "storCd":"${_storCd.value}", "cornerCd":"${_cornerCd.value}", "deviceType":"DID"}"""
        sendMessage(subscribeMessage)
    }

    private fun subscribeOrderEvents() {
        val subscribeMessage = """{ "type": "subscribe", "topic": "order", "deviceId": "${_androidId.value}", "salesOrgCd":"${_salesOrgCd.value}", "storCd":"${_storCd.value}", "cornerCd":"${_cornerCd.value}", "deviceType":"DID"}"""
        sendMessage(subscribeMessage)
    }

    private fun subscribeRestartEvents() {
        val subscribeMessage = """{ "type": "subscribe", "topic": "RESTART", "deviceId": "${_androidId.value}"}"""
        sendMessage(subscribeMessage)
    }


    private fun subscribeToEvents() {
        val subscribeMessage = """{ "type": "subscribe", "topic": "ECHO", "userId": "5000511001" }"""
        sendMessage(subscribeMessage) // 구독 요청 전송
    }

    private fun subscribeProductEvents() {
        val subscribeMessage = """{ "type": "subscribe", "topic": "PRODUCT", "userId": "rest" }"""
        sendMessage(subscribeMessage)
    }   //변경분 수신

    private fun subscribeProductMasterEvents() {
        val subscribeMessage = """{ "type": "subscribe", "topic": "PRODUCT", "userId": "rest" }"""
        sendMessage(subscribeMessage)
    }   //전체 product 수신 요청을 위한 구독(Product, Detail)

    private fun observeNetworkChanges() {
        val connectivityManager =
            getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                Log.d(tag, "Network available, reconnecting...")

                viewModelScope.launch {
                    delay(1000) // 네트워크 복구 후 안정화 대기
                    forceReconnect()
                }
            }

            override fun onLost(network: Network) {
                Log.e(tag, "Network lost")
                isConnected = false
            }
        })
    }
    private fun forceReconnect() {
        if (reconnectJob?.isActive == true) return // 이미 재연결 진행 중이면 중복 실행 방지

        reconnectJob = viewModelScope.launch {
            WebSocketManager.close() // 기존 소켓 강제 종료
            webSocket = null
            isConnected = false

            delay(2000) // 2초 후 재연결 시도
            connectWebSocket()
        }
    }

    sealed interface UiState {
        object UpdateDevice : UiState
        object CheckDevice : UiState
        object UpdateVersion : UiState
        object Idle : UiState
        data class SoldOut(val data: String) : UiState
        data class InsertOrder(val data: String) : UiState
    }
}