package com.secta9ine.rest.did.data.remote.dto

data class RestApiResponseDto<T> (
    val responseBody: T? = null,
    val responseCode: String? = null,
    val responseMessage: String? = null
)