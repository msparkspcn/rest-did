package com.secta9ine.rest.did.data.remote.api

import com.secta9ine.rest.did.data.remote.dto.LoginRequestDto
import com.secta9ine.rest.did.data.remote.dto.RestApiRequestDto
import com.secta9ine.rest.did.data.remote.dto.RestApiResponseDto
import com.secta9ine.rest.did.domain.model.OrderStatus
import com.secta9ine.rest.did.domain.model.Stor
import com.secta9ine.rest.did.domain.model.User
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface RestApiService {
//    @FormUrlEncoded
    @POST("/api/v1/store/getStoreInfo")
    suspend fun getStoreInfo (
        @Query("authPassword") authPassword: String,
        @Query("storeCd") storeCd: String
    ):RestApiResponseDto<Stor>

    @POST("/api/v1/login")
    suspend fun acceptLogin (
        @Body body: LoginRequestDto
    ):RestApiResponseDto<User>

    @POST("/api/v1/did/order")
    suspend fun getOrderList (
        @Body body: RestApiRequestDto
    ):RestApiResponseDto<List<OrderStatus>>

    companion object {
//        private const val BASE_URL = "http://110.45.199.220:17070/" // 운영서버
        private const val BASE_URL = "https://s9rest.ngrok.io/" // 개발서버

        fun create(): RestApiService {
            val logger =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(AuthInterceptor("abcd12345"))
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RestApiService::class.java)
        }
    }
}