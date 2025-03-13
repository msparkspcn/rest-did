package com.secta9ine.rest.did.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun clearAll()
    fun getUserId(): Flow<String>
    suspend fun setUserId(value: String)
    fun getStorCd(): Flow<String>
    suspend fun setStorCd(value: String)
    fun getCornerCd(): Flow<String>
    suspend fun setCornerCd(value: String)
    fun getDeviceNo(): Flow<String>
    suspend fun setDeviceNo(value: String)

    fun getPassword(): Flow<String>
    suspend fun setPassword(value: String)

    fun getCmpCd(): Flow<String>
    suspend fun setCmpCd(value: String)
    fun getSalesOrgCd(): Flow<String>
    suspend fun setSalesOrgCd(value: String)
    fun getUserRoleType(): Flow<String>
    suspend fun setUserRoleType(value: String)
    fun getIsAutoLoginChecked(): Flow<String>
    suspend fun setIsAutoLoginChecked(value: String)
}