package com.secta9ine.rest.did.network

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONException
import org.json.JSONObject

class WebSocketViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = this.javaClass.simpleName
    private var webSocket: WebSocket? = null
    private var isConnected = false
    private var retryJob: Job? = null
    private var reconnectJob: Job? = null
    init {
//        observeNetworkChanges()
//        connectWebSocket()
    }

    private fun connectWebSocket() {
        if (isConnected) {
            Log.d(TAG,"already connected")
            return
        }
        webSocket?.close(1000, "Reconnecting") // 기존 WebSocket 정리
        webSocket = null

        WebSocketManager.connect(object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d(TAG, "Connected") //connect 후 구독 요청
                this@WebSocketViewModel.webSocket = webSocket
                isConnected = true
                subscribeToEvents()
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    Log.d(TAG, "Message received: $text")
                    /*
                    val jsonObject =JSONObject(text)
                    val event = jsonObject.getString("event")

                    if(event == "order_updated") {
                        val data = jsonObject.getJSONObject("data")

                    }
                    
                     */
                } catch (e: JSONException) {
                    Log.d(TAG,"JSON Parsing Error:${e.message}")
                }



            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e(TAG, "Error: ${t.message ?: "Unknown error"}")

                isConnected = false
                WebSocketManager.close() // WebSocketManager의 웹소켓도 정리
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

    fun sendMessage(message: String) {
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

    private fun subscribeToEvents() {
        val subscribeMessage = """{ "type": "subscribe", "topic": "ECHO", "userId": "rest" }"""
        sendMessage(subscribeMessage) // 구독 요청 전송
    }

    private fun subscribeOrderEvents() {
        val subscribeMessage = """{ "type": "subscribe", "topic": "ORDER", "cmpCd": "SLKR", "salesOrgCd": "8000", "storCd": "5000511", "cornerCd": "CIBA" }"""
        sendMessage(subscribeMessage)
    }

    private fun subscribeProductEvents() {
        val subscribeMessage = """{ "type": "subscribe", "topic": "PRODUCT", "userId": "rest" }"""
        sendMessage(subscribeMessage)
    }


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
        if (reconnectJob?.isActive == true) return // 이미 재연결이 진행 중이면 중복 실행 방지

        reconnectJob = viewModelScope.launch {
            WebSocketManager.close() // 기존 소켓 강제 종료
            webSocket = null
            isConnected = false

            delay(2000) // 2초 후 재연결 시도
            connectWebSocket()
        }
    }
}