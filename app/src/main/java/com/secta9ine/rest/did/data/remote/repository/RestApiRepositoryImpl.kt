package com.secta9ine.rest.did.data.remote.repository

import android.content.res.Resources
import com.secta9ine.rest.did.R
import com.secta9ine.rest.did.data.remote.api.RestApiService
import com.secta9ine.rest.did.data.remote.dto.CmpRequestDto
import com.secta9ine.rest.did.data.remote.dto.LoginRequestDto
import com.secta9ine.rest.did.data.remote.dto.RestApiRequestDto
import com.secta9ine.rest.did.domain.model.Cmp
import com.secta9ine.rest.did.domain.model.OrderStatus
import com.secta9ine.rest.did.domain.model.Stor
import com.secta9ine.rest.did.domain.model.User
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
    ): Resource<Stor?> = withContext(Dispatchers.IO){
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

    override suspend fun acceptLogin(
        userId: String,
        password: String
    ): Resource<User?> = withContext(Dispatchers.IO){
        try {
            val requestBody = LoginRequestDto(
                userId = userId,
                password = password
            )
            restApiService.acceptLogin(requestBody).let {
                Resource.Success(it.responseBody)
            }
        } catch (e: Exception) {
            Resource.Failure(resources.getString(R.string.network_error))
        }
    }

    override suspend fun getOrderList(
        cmpCd: String,
        salesOrgCd: String,
        storCd: String,
        cornerCd: String
    ): Resource<List<OrderStatus?>> = withContext(Dispatchers.IO){
        try {
            val requestBody = RestApiRequestDto(
                cmpCd =  cmpCd,
                salesOrgCd = salesOrgCd,
                storCd = storCd,
                cornerCd = cornerCd
            )
            restApiService.getOrderList(requestBody).let {
                Resource.Success(it.responseBody)
            }
        } catch (e: Exception) {
            Resource.Failure(resources.getString(R.string.network_error))
        }
    }

    override suspend fun getCmp(
        cmpCd: String
    ): Resource<List<Cmp>> = withContext(Dispatchers.IO){
        try {
            restApiService.getCmp(CmpRequestDto(cmpCd = cmpCd)).let {
                Resource.Success(it.responseBody)
            }
        } catch (e: Exception) {
            Resource.Failure(resources.getString(R.string.network_error))
        }
    }

    override suspend fun getCmp2(
        cmpCd: String
    ): List<Cmp> {
        return restApiService.getCmp2(CmpRequestDto(cmpCd = cmpCd))
    }

}