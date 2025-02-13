package com.secta9ine.rest.did.network

import okhttp3.*;
import java.util.concurrent.TimeUnit

object WebSocketManager {
    private var webSocket: WebSocket? = null
    private val client: OkHttpClient = OkHttpClient.Builder()
        .pingInterval(30, TimeUnit.SECONDS)
        .build()
    private const val WEBSOCKET_URL = "wss://echo.websocket.org"

    fun connect(listener: WebSocketListener) {
        if (webSocket == null) {
            val request = Request.Builder().url(WEBSOCKET_URL).build()
            webSocket = client.newWebSocket(request, listener)
        }
    }

    fun sendMessage(message: String) {
        webSocket?.send(message)
    }

    fun close() {
        webSocket?.close(1000, "Closed by user")
        webSocket = null
    }
}