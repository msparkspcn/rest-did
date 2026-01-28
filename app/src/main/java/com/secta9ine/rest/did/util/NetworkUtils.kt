package com.secta9ine.rest.did.util

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(
    call: suspend () -> T
): Resource<T> = withContext(Dispatchers.IO) {
    try {
        Resource.Success(call())
    } catch (e: Exception) {
        Log.e("safeApiCall", "Error: ${e.message}", e)
        Resource.Failure(handleError(e))
    }
}

suspend fun <T> safeApiResponseCall(
    call: suspend () -> com.secta9ine.rest.did.data.remote.dto.RestApiResponseDto<T>
): Resource<T> = withContext(Dispatchers.IO) {
    try {
        val response = call()
        if (response.responseCode == "200") {
            Resource.Success(response.responseBody)
        } else {
            Resource.Failure(response.responseMessage ?: "Error: ${response.responseCode}")
        }
    } catch (e: Exception) {
        Log.e("safeApiResponseCall", "Error: ${e.message}", e)
        Resource.Failure(handleError(e))
    }
}

private fun handleError(e: Exception): String {
    return when (e) {
        is IOException -> "Network error occurred"
        is HttpException -> {
            when (e.code()) {
                401 -> "Unauthorized access"
                404 -> "Resource not found"
                else -> "Unknown network error: ${e.code()}"
            }
        }
        else -> e.message ?: "Unknown error occurred"
    }
}
