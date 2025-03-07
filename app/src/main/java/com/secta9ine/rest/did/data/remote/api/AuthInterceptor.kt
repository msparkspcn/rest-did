package com.secta9ine.rest.did.data.remote.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
private val TAG = "AuthInterceptor"
class AuthInterceptor(private var authToken: String? = null) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequestBuilder = originalRequest.newBuilder()

        authToken?.let {
            Log.d("AuthInterceptor", "Using authToken: $it")
            newRequestBuilder.addHeader("Authorization", it)
        }

        val newRequest = newRequestBuilder.build()
        return chain.proceed(newRequest)
    }
    fun setAuthToken(token: String) {
        Log.d(TAG,"token:$token")
        this.authToken = token
    }
}