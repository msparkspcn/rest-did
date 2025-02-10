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

    override fun getStoreCd(): Flow<String> = getValue(STORE_CD, "")
    override suspend fun setStoreCd(value: String) = setValue(STORE_CD, value)
    override fun getStorePassword(): Flow<String> = getValue(STORE_PASSWORD, "");

    override suspend fun setStorePassword(value: String) = setValue(STORE_PASSWORD, value)

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
        val STORE_CD = stringPreferencesKey("STORE_CD")
        val STORE_PASSWORD = stringPreferencesKey("STORE_PASSWORD")
    }
}