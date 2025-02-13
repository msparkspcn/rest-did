package com.secta9ine.rest.did.network

import android.util.Log
import androidx.lifecycle.ViewModel
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebSocketViewModel : ViewModel() {
    init {
        connectWebSocket()
    }

    private fun connectWebSocket() {
        WebSocketManager.connect(object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("WebSocket", "Connected")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocket", "Message received: $text")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("WebSocket", "Error: ${t.message}")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("WebSocket", "Closed: $reason")
            }
        })
    }

    fun sendMessage(message: String) {
        WebSocketManager.sendMessage(message)
    }

    override fun onCleared() {
        super.onCleared()
        WebSocketManager.close()
    }
}