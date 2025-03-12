package com.secta9ine.rest.did.network

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.provider.Settings
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.secta9ine.rest.did.data.remote.api.RestApiService
import com.secta9ine.rest.did.domain.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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
@Inject constructor
    (
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {
    private val TAG = this.javaClass.simpleName
    private var webSocket: WebSocket? = null
    private var isConnected = false
    private var retryJob: Job? = null
    private var reconnectJob: Job? = null
    private val _uiState = MutableSharedFlow<UiState>()
    val uiState = _uiState.asSharedFlow()
    var androidId by mutableStateOf("")
        private set
    init {
        uiState.onEach { Log.d(TAG, "uiState=$it") }.launchIn(viewModelScope)
        observeNetworkChanges()
        connectWebSocket()
    }

    private fun connectWebSocket() {
        Log.d(TAG,"connectWebSocket")
        androidId = Settings.Secure.getString(getApplication<Application>().contentResolver, Settings.Secure.ANDROID_ID)

        Log.d(TAG, "ANDROID_ID: $androidId")
        if (isConnected) {
            Log.d(TAG,"already connected")
            return
        }

        WebSocketManager.connect(object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d(TAG, "Connected") //connect 후 구독 요청
                this@WebSocketViewModel.webSocket = webSocket
                isConnected = true
                subscribeDeviceEvent() //장비 이벤트 구독(cmp,sales,stor,corner,deviceNo,displayMenuCd,rollingYn,apiKey 변경 여부 감지)
//                subscribeRestartEvents() //재실행 이벤트 구독
                //DID 상품 이벤트 구독
                //DID 상품 부가정보 이벤트 구독
                //주문 이벤트 구독
                subscribeToEvents()
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    Log.d(TAG, "Message received: $text")

                    /**/
                    val jsonObject =JSONObject(text)
                    val event = jsonObject.getString("type")
                    val message = jsonObject.getString("message")
                    if(event == "ECHO") {
                        Log.d(TAG, "앱 재실행")
                        exitProcess(0)
                    }
                    if(event == "ACTIVE") {     //장비 활성화
                        Log.d(TAG,"message:$message")
                        viewModelScope.launch {
                            setDeviceInfo(JSONObject(message))
                            _uiState.emit(UiState.UpdateDevice)
                        }
                    }

                    if(event == "order_updated") {
                        val data = jsonObject.getJSONObject("data")
                    }
                } catch (e: JSONException) {
                    Log.d(TAG,"JSON Parsing Error:${e.message}")
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e(TAG, "Error: ${t.message ?: "Unknown error"}")

                isConnected = false
                WebSocketManager.close()
                this@WebSocketViewModel.webSocket = null

                Log.d(TAG, "Attempting to retry connection in 5 seconds...")
                retryWebSocket()
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.d(TAG, "Closed: $reason")
                isConnected = false
                if(code != 1000) {
                    retryWebSocket() // 수동으로 종료되지 않았다면 재연결
                }
            }
        })
    }


    private suspend fun setDeviceInfo(messageObject:JSONObject) {

        dataStoreRepository.setCmpCd(messageObject.getString("cmpCd"))
//                        dataStoreRepository.setSalesOrgCd(messageObject.getString("salesOrgCd"))
        dataStoreRepository.setStorCd(messageObject.getString("storCd"))
        dataStoreRepository.setCornerCd(messageObject.getString("cornerCd"))
        dataStoreRepository.setDeviceNo(messageObject.getString("deviceNo"))
        dataStoreRepository.setDeviceNo(messageObject.getString("deviceNo"))
        RestApiService.updateAuthToken(messageObject.getString("apiKey"))
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
        val subscribeMessage = """{"type": "subscribe", "topic":"DEVICE", "deviceId": "$androidId"}"""
        sendMessage(subscribeMessage)
    }
    //구독해지(UserId

    private fun subscribeRestartEvents() {
        val subscribeMessage = """{ "type": "subscribe", "topic": "RESTART", "deviceId": "$androidId"}"""
        sendMessage(subscribeMessage)
    }


    private fun subscribeToEvents() {
        val subscribeMessage = """{ "type": "subscribe", "topic": "ECHO", "userId": "5000511001" }"""
        sendMessage(subscribeMessage) // 구독 요청 전송
    }

    private fun subscribeOrderEvents() {
        val subscribeMessage = """{ "type": "subscribe", "topic": "ORDER", "cmpCd": "SLKR", "salesOrgCd": "8000", "storCd": "5000511", "cornerCd": "CIBA" }"""
        sendMessage(subscribeMessage)
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
                Log.d(TAG, "Network available, reconnecting...")

                viewModelScope.launch {
                    delay(1000) // 네트워크 복구 후 안정화 대기
                    forceReconnect()
                }
            }

            override fun onLost(network: Network) {
                Log.e(TAG, "Network lost")
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
        object Idle : UiState
    }
}