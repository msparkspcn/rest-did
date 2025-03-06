package com.secta9ine.rest.did.data.remote.api

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val authToken: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .addHeader("Authorization", authToken) // Bearer 토큰 형식
            .build()
        return chain.proceed(newRequest)
    }
}