package com.secta9ine.rest.did.data.remote.repository

import com.secta9ine.rest.did.data.remote.api.RestApiService
import com.secta9ine.rest.did.data.remote.dto.*
import com.secta9ine.rest.did.domain.model.*
import com.secta9ine.rest.did.domain.repository.RestApiRepository
import com.secta9ine.rest.did.util.Resource
import com.secta9ine.rest.did.util.safeApiResponseCall
import javax.inject.Inject

class RestApiRepositoryImpl @Inject constructor(
    private val restApiService: RestApiService
): RestApiRepository {
    override suspend fun getStoreInfo(
        storeCd: String,
        storePassword: String
    ): Resource<Stor?> = safeApiResponseCall {
        restApiService.getStoreInfo(
            storeCd = storeCd,
            authPassword = storePassword
        ).let { RestApiResponseDto(it.responseBody, it.responseCode, it.responseMessage) }
    }

    override suspend fun acceptLogin(
        userId: String,
        password: String
    ): Resource<User?> = safeApiResponseCall {
        val requestBody = LoginRequestDto(
            userId = userId,
            password = password
        )
        restApiService.acceptLogin(requestBody).let {
            RestApiResponseDto(it.responseBody, it.responseCode, it.responseMessage)
        }
    }

    override suspend fun registerDeviceId(
        deviceId: String
    ): Resource<Device> = safeApiResponseCall {
        restApiService.registerDeviceId(deviceId)
    }

    override suspend fun checkDevice(
        deviceId: String
    ): Resource<Device> = safeApiResponseCall {
        restApiService.checkDevice(deviceId)
    }

    override suspend fun setDevice(
        deviceId: String,
        cmpCd: String,
        salesOrgCd: String,
        storCd: String,
        cornerCd: String,
        deviceNo: String
    ): Resource<Unit> = safeApiResponseCall {
        val requestBody = DeviceRequestDto(
            cmpCd =  cmpCd,
            salesOrgCd = salesOrgCd,
            storCd = storCd,
            cornerCd = cornerCd,
            deviceId = deviceId,
            deviceNo = deviceNo
        )
        restApiService.setDevice(requestBody)
    }

    override suspend fun getOrderList(
        cmpCd: String,
        salesOrgCd: String,
        storCd: String,
        cornerCd: String,
        saleDt: String,
    ): Resource<List<OrderStatus?>> = safeApiResponseCall {
        val requestBody = OrderStatusRequestDto(
            cmpCd =  cmpCd,
            salesOrgCd = salesOrgCd,
            storCd = storCd,
            cornerCd = cornerCd,
            searchDt = saleDt
        )
        restApiService.getOrderList(requestBody).let {
            RestApiResponseDto(it.responseBody?.map { item -> item as OrderStatus? }, it.responseCode, it.responseMessage)
        }
    }

    override suspend fun getCmpList(
        cmpCd: String
    ): Resource<List<Cmp>> = safeApiResponseCall {
        restApiService.getCmpList(CmpRequestDto(cmpValue = cmpCd))
    }

    override suspend fun geSalesOrgList(
        cmpCd: String
    ): Resource<List<SalesOrg>> = safeApiResponseCall {
        restApiService.getSalesOrgList(SalesOrgRequestDto(cmpCd = cmpCd, restValue = ""))
    }

    override suspend fun geStorList(
        cmpCd: String,
        salesOrgCd: String
    ): Resource<List<Stor>> = safeApiResponseCall {
        restApiService.getStorList(StorRequestDto(cmpCd = cmpCd, salesOrgCd = salesOrgCd, storeValue = ""))
    }

    override suspend fun getCornerList(
        cmpCd: String,
        salesOrgCd: String,
        storCd: String
    ): Resource<List<Corner>> = safeApiResponseCall {
        restApiService.getCornerList(
            CornerRequestDto(
                cmpCd = cmpCd,
                salesOrgCd = salesOrgCd,
                storCd = storCd,
            )
        )
    }

    override suspend fun getCornerInfo(
        cmpCd: String,
        salesOrgCd: String,
        storCd: String,
        cornerCd: String
    ): Resource<Corner> = safeApiResponseCall {
        restApiService.getCornerInfo(
            RestApiRequestDto(
                cmpCd = cmpCd,
                salesOrgCd = salesOrgCd,
                storCd = storCd,
                cornerCd = cornerCd,
            )
        )
    }


    override suspend fun getProductList(
        cmpCd: String,
        salesOrgCd: String,
        storCd: String,
        cornerCd: String
    ): Resource<List<Product>>  = safeApiResponseCall {
        restApiService.getProductList(
            RestApiRequestDto(
                cmpCd = cmpCd,
                salesOrgCd = salesOrgCd,
                storCd = storCd,
                cornerCd = cornerCd
            )
        )
    }

    override suspend fun getSaleOpen(
        cmpCd: String,
        salesOrgCd: String,
        storCd: String
    ): Resource<SaleOpen> = safeApiResponseCall {
        restApiService.getSaleOpen(
            SaleOpenRequestDto(
                cmpCd = cmpCd,
                salesOrgCd = salesOrgCd,
                storCd = storCd
            )
        )
    }
}