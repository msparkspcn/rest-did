package com.secta9ine.rest.did.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun clearAll()
    fun getUserId(): Flow<String>
    suspend fun setUserId(value: String)
    fun getStoreCd(): Flow<String>
    suspend fun setStoreCd(value: String)

    fun getPassword(): Flow<String>
    suspend fun setPassword(value: String)
}