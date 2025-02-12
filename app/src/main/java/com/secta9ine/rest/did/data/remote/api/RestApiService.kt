package com.secta9ine.rest.did.data.remote.api

import androidx.datastore.preferences.protobuf.Api
import com.secta9ine.rest.did.data.remote.dto.RestApiResponseDto
import com.secta9ine.rest.did.domain.model.Store
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

interface RestApiService {
//    @FormUrlEncoded
    @POST("/api/v1/store/getStoreInfo")
    suspend fun getStoreInfo (
        @Query("authPassword") authPassword: String,
        @Query("storeCd") storeCd: String
    ):RestApiResponseDto<Store>
    companion object {
        private const val BASE_URL = "http://110.45.199.220:17070/" // 운영서버

        fun create(): RestApiService {
            val logger =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
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