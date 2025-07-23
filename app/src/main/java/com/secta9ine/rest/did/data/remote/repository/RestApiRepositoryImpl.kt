package com.secta9ine.rest.did.data.remote.repository

import android.content.res.Resources
import android.util.Log
import com.secta9ine.rest.did.R
import com.secta9ine.rest.did.data.remote.api.RestApiService
import com.secta9ine.rest.did.data.remote.dto.CmpRequestDto
import com.secta9ine.rest.did.data.remote.dto.CornerRequestDto
import com.secta9ine.rest.did.data.remote.dto.DeviceRequestDto
import com.secta9ine.rest.did.data.remote.dto.LoginRequestDto
import com.secta9ine.rest.did.data.remote.dto.OrderStatusRequestDto
import com.secta9ine.rest.did.data.remote.dto.RestApiRequestDto
import com.secta9ine.rest.did.data.remote.dto.SaleOpenRequestDto
import com.secta9ine.rest.did.data.remote.dto.SalesOrgRequestDto
import com.secta9ine.rest.did.data.remote.dto.StorRequestDto
import com.secta9ine.rest.did.domain.model.Cmp
import com.secta9ine.rest.did.domain.model.Corner
import com.secta9ine.rest.did.domain.model.Device
import com.secta9ine.rest.did.domain.model.OrderStatus
import com.secta9ine.rest.did.domain.model.Product
import com.secta9ine.rest.did.domain.model.SaleOpen
import com.secta9ine.rest.did.domain.model.SalesOrg
import com.secta9ine.rest.did.domain.model.Stor
import com.secta9ine.rest.did.domain.model.User
import com.secta9ine.rest.did.domain.repository.RestApiRepository
import com.secta9ine.rest.did.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
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
            val response = restApiService.acceptLogin(requestBody)
            when(response.responseCode) {
                "200" -> {
                    Resource.Success(response.responseBody)
                }
                "404" -> {
                    Resource.Failure(response.responseMessage!!)
                }
                "401" -> {
                    Resource.Failure(response.responseMessage!!)
                }
                else -> {
                    Resource.Failure(resources.getString(R.string.network_error))
                }
            }
        }
        catch (e: Exception) {
            Resource.Failure(resources.getString(R.string.network_error))
        }
    }

    override suspend fun registerDeviceId(
        deviceId: String
    ): Resource<Device> = withContext(Dispatchers.IO){
        try {

            restApiService.registerDeviceId(deviceId).let {
                Resource.Success(it.responseBody)
            }
        } catch (e: Exception) {
            Resource.Failure(resources.getString(R.string.network_error))
        }
    }

    override suspend fun checkDevice(
        deviceId: String
    ): Resource<Device> = withContext(Dispatchers.IO){
        try {
            val response = restApiService.checkDevice(deviceId)
            when(response.responseCode) {
                "200" -> {
                    Resource.Success(response.responseBody)
                }
                "404" -> {
                    Resource.Failure(response.responseMessage!!)
                }
                else -> {
                    Resource.Failure(resources.getString(R.string.network_error))
                }
            }

        } catch (e: Exception) {
            Resource.Failure(resources.getString(R.string.network_error))
        }
    }

    override suspend fun setDevice(
        deviceId: String,
        cmpCd: String,
        salesOrgCd: String,
        storCd: String,
        cornerCd: String,
        deviceNo: String
    ): Resource<Unit> = withContext(Dispatchers.IO){
        try {
            val requestBody = DeviceRequestDto(
                cmpCd =  cmpCd,
                salesOrgCd = salesOrgCd,
                storCd = storCd,
                cornerCd = cornerCd,
                deviceId = deviceId,
                deviceNo = deviceNo
            )
            restApiService.setDevice(requestBody).let {
                Resource.Success(it.responseBody)
            }
        }
        catch (e :Exception) {
            Resource.Failure(resources.getString(R.string.network_error))
        }
    }

    override suspend fun getOrderList(
        cmpCd: String,
        salesOrgCd: String,
        storCd: String,
        cornerCd: String,
        saleDt: String,
    ): Resource<List<OrderStatus?>> = withContext(Dispatchers.IO){
        try {
            val requestBody = OrderStatusRequestDto(
                cmpCd =  cmpCd,
                salesOrgCd = salesOrgCd,
                storCd = storCd,
                cornerCd = cornerCd,
                searchDt = saleDt
            )
            restApiService.getOrderList(requestBody).let {
                Resource.Success(it.responseBody)
            }
        } catch (e: Exception) {
            Resource.Failure(resources.getString(R.string.network_error))
        }
    }

    override suspend fun getCmpList(
        cmpCd: String
    ): Resource<List<Cmp>> = withContext(Dispatchers.IO){
        try {
            val response = restApiService.getCmpList(CmpRequestDto(cmpValue = cmpCd))
            
            Resource.Success(response.responseBody)

        }
        catch (e: HttpException) {
            when(e.code()) {
                401 -> {
                    Log.d("Impl", "Unauthorized access: ${e.message()}")
                    Resource.Failure(resources.getString(R.string.unauthorized_error))
                }
                else -> {
                    Resource.Failure(resources.getString(R.string.network_error))
                }
            }
        }
        catch (e: Exception) {
            Log.d("Impl", "e:$e")
            Resource.Failure(resources.getString(R.string.network_error))
        }
    }

    override suspend fun geSalesOrgList(
        cmpCd: String
    ): Resource<List<SalesOrg>> = withContext(Dispatchers.IO) {
        try {
            val response = restApiService.getSalesOrgList(SalesOrgRequestDto(cmpCd = cmpCd, restValue = ""))

            Resource.Success(response.responseBody)
        }
        catch(e: Exception) {
            Resource.Failure(resources.getString(R.string.network_error))
        }
    }

    override suspend fun geStorList(
        cmpCd: String,
        salesOrgCd: String
    ): Resource<List<Stor>> = withContext(Dispatchers.IO) {
        try {
            val response = restApiService.getStorList(StorRequestDto(cmpCd = cmpCd, salesOrgCd = salesOrgCd, storeValue = ""))

            Resource.Success(response.responseBody)
        }
        catch (e: Exception) {
            Resource.Failure(resources.getString(R.string.network_error))
        }
    }

    override suspend fun getCornerList(
        cmpCd: String,
        salesOrgCd: String,
        storCd: String
    ): Resource<List<Corner>> = withContext(Dispatchers.IO) {
        try {
            restApiService.getCornerList(
                CornerRequestDto(
                    cmpCd = cmpCd,
                    salesOrgCd = salesOrgCd,
                    storCd = storCd,
                )).let {
                Resource.Success(it.responseBody)
            }
        } catch (e: Exception) {
            Resource.Failure(resources.getString(R.string.network_error))
        }
    }

    override suspend fun getProductList(
        cmpCd: String,
        salesOrgCd: String,
        storCd: String,
        cornerCd: String
    ): Resource<List<Product>>  = withContext(Dispatchers.IO) {
        try {
            restApiService.getProductList(
                RestApiRequestDto(
                    cmpCd = cmpCd,
                    salesOrgCd = salesOrgCd,
                    storCd = storCd,
                    cornerCd = cornerCd
                )).let {
                    Resource.Success(it.responseBody)
            }
        } catch (e: Exception) {
            Resource.Failure(resources.getString(R.string.network_error))
        }
    }

    override suspend fun getSaleOpen(
        cmpCd: String,
        salesOrgCd: String,
        storCd: String
    ): Resource<SaleOpen> = withContext(Dispatchers.IO) {
        try {
            restApiService.getSaleOpen(
                SaleOpenRequestDto(
                    cmpCd = cmpCd,
                    salesOrgCd = salesOrgCd,
                    storCd = storCd
                )
            ).let {
                Resource.Success(it.responseBody)
            }
        } catch (e: Exception) {
            Resource.Failure(resources.getString(R.string.network_error))
        }
    }

}