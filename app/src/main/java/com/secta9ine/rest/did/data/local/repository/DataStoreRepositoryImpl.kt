package com.secta9ine.rest.did.data.local.repository

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.secta9ine.rest.did.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private const val DATASTORE_NAME = "did.datastore"
val Application.dataStore: DataStore<Preferences> by preferencesDataStore(DATASTORE_NAME)
class DataStoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
): DataStoreRepository {
    override suspend fun clearAll() {
        dataStore.edit { it.clear() }
    }

    override fun getUserId(): Flow<String> = getValue(USER_ID, "")

    override suspend fun setUserId(value: String) { setValue(USER_ID, value) }

    override fun getStorCd(): Flow<String> = getValue(STOR_CD, "")
    override suspend fun setStorCd(value: String) = setValue(STOR_CD, value)
    override fun getCornerCd(): Flow<String> = getValue(CORNER_CD, "")

    override suspend fun setCornerCd(value: String) = setValue(CORNER_CD, value)
    override fun getDeviceNo(): Flow<String> = getValue(DEVICE_NO, "")

    override suspend fun setDeviceNo(value: String) = setValue(DEVICE_NO, value)

    override fun getPassword(): Flow<String> = getValue(STORE_PASSWORD, "")

    override suspend fun setPassword(value: String) = setValue(STORE_PASSWORD, value)
    override fun getCmpCd(): Flow<String> = getValue(CMP_CD, "")
    override suspend fun setCmpCd(value: String) = setValue(CMP_CD, value)

    override fun getSalesOrgCd(): Flow<String> = getValue(SALES_ORG_CD, "")

    override suspend fun setSalesOrgCd(value: String) = setValue(SALES_ORG_CD, value)
    override fun getUserRoleType(): Flow<String> = getValue(USER_ROLE_TYPE, "")

    override suspend fun setUserRoleType(value: String) = setValue(USER_ROLE_TYPE, value)
    override fun getIsAutoLoginChecked(): Flow<String> = getValue(IS_AUTO_LOGIN_CHECKED, "N")

    override suspend fun setIsAutoLoginChecked(value: String) = setValue(IS_AUTO_LOGIN_CHECKED, value)

    private fun <T> getValue(key: Preferences.Key<T>, defValue: T): Flow<T> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[key] ?: defValue
            }
    }

    private suspend fun <T> setValue(key: Preferences.Key<T>, value: T) {
        dataStore.edit { it[key] = value }
    }

    companion object {
        val CMP_CD = stringPreferencesKey("CMP_CD")
        val SALES_ORG_CD = stringPreferencesKey("SALES_ORG_CD")
        val STOR_CD = stringPreferencesKey("STOR_CD")
        val CORNER_CD = stringPreferencesKey("CORNER_CD")
        val DEVICE_NO = stringPreferencesKey("DEVICE_NO")
        val USER_ROLE_TYPE = stringPreferencesKey("USER_ROLE_TYPE")
        val STORE_PASSWORD = stringPreferencesKey("STOR_PASSWORD")
        val USER_ID = stringPreferencesKey("USER_ID")
        val IS_AUTO_LOGIN_CHECKED = stringPreferencesKey("IS_AUTO_LOGIN_CHECKED")
    }
}