package com.secta9ine.rest.did.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    fun getStoreCd(): Flow<String>
    suspend fun setStoreCd(value: String)

    fun getStorePassword(): Flow<String>
    suspend fun setStorePassword(value: String)
}