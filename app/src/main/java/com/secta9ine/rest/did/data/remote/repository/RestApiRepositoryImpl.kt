package com.secta9ine.rest.did.data.remote.repository

import android.content.res.Resources
import com.secta9ine.rest.did.R
import com.secta9ine.rest.did.data.remote.api.RestApiService
import com.secta9ine.rest.did.domain.model.Store
import com.secta9ine.rest.did.domain.repository.RestApiRepository
import com.secta9ine.rest.did.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RestApiRepositoryImpl @Inject constructor(
    private val resources: Resources,
    private val restApiService: RestApiService
): RestApiRepository {
    override suspend fun getStoreInfo(
        storeCd: String,
        storePassword: String
    ): Resource<Store?> = withContext(Dispatchers.IO){
        try {
            restApiService.getStoreInfo(
                storeCd = storeCd,
                authPassword = storePassword
            ).let {
                Resource.Success(it.responseBody)
            }
        } catch (e: Exception) {
            Resource.Failure(resources.getString(R.string.network_error))
        }
    }
}