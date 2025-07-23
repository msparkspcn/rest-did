package com.secta9ine.rest.did.domain.repository

import com.secta9ine.rest.did.domain.model.Cmp
import com.secta9ine.rest.did.domain.model.Corner
import com.secta9ine.rest.did.domain.model.Device
import com.secta9ine.rest.did.domain.model.OrderStatus
import com.secta9ine.rest.did.domain.model.Product
import com.secta9ine.rest.did.domain.model.SaleOpen
import com.secta9ine.rest.did.domain.model.SalesOrg
import com.secta9ine.rest.did.domain.model.Stor
import com.secta9ine.rest.did.domain.model.User
import com.secta9ine.rest.did.util.Resource

interface RestApiRepository {
    suspend fun getStoreInfo(storeCd: String, storePassword: String): Resource<Stor?>
    suspend fun acceptLogin(userId: String, password: String): Resource<User?>
    suspend fun registerDeviceId(deviceId: String): Resource<Device>
    suspend fun checkDevice(deviceId: String): Resource<Device>
    suspend fun setDevice(deviceId: String, cmpCd: String, salesOrgCd:
    String, storCd: String, cornerCd: String, deviceNo: String): Resource<Unit>
    suspend fun getOrderList(cmpCd: String, salesOrgCd: String, storCd: String, cornerCd: String, saleDt: String): Resource<List<OrderStatus?>>
    suspend fun getCmpList(cmpCd: String): Resource<List<Cmp>>
    suspend fun geSalesOrgList(cmpCd: String): Resource<List<SalesOrg>>
    suspend fun geStorList(cmpCd: String, salesOrgCd: String): Resource<List<Stor>>
    suspend fun getCornerList(cmpCd: String, salesOrgCd: String, storCd: String): Resource<List<Corner>>
    suspend fun getProductList(cmpCd: String, salesOrgCd: String, storCd: String, cornerCd: String): Resource<List<Product>>
    suspend fun getSaleOpen(cmpCd: String, salesOrgCd: String, storCd: String): Resource<SaleOpen>
}