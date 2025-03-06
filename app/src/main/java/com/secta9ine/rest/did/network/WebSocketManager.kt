package com.secta9ine.rest.did.network

import android.util.Log
import okhttp3.*;
import java.util.concurrent.TimeUnit

object WebSocketManager {
    private var webSocket: WebSocket? = null
    private val client: OkHttpClient = OkHttpClient.Builder()
        .pingInterval(30, TimeUnit.SECONDS)
        .build()
    private const val WEBSOCKET_URL = "ws://10.120.44.88:8082/ws"

    fun connect(listener: WebSocketListener) {
        // 기존 WebSocket이 남아있다면 닫기
        webSocket?.close(1000, "Reconnecting")
        webSocket = null

        val request = Request.Builder().url(WEBSOCKET_URL).build()
        webSocket = client.newWebSocket(request, listener)
    }

    fun sendMessage(message: String) {
        Log.d("WebSocketManager", "sendMessage11:$message")
        webSocket?.send(message)
    }

    fun close() {
        webSocket?.close(1000, "Closed by user")
        webSocket = null
    }
}