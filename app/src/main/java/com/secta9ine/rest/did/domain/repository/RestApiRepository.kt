package com.secta9ine.rest.did.domain.repository

import com.secta9ine.rest.did.domain.model.OrderStatus
import com.secta9ine.rest.did.domain.model.Stor
import com.secta9ine.rest.did.domain.model.User
import com.secta9ine.rest.did.util.Resource

interface RestApiRepository {
    suspend fun getStoreInfo(storeCd: String, storePassword: String): Resource<Stor?>
    suspend fun acceptLogin(userId: String, password: String): Resource<User?>
    suspend fun getOrderList(cmpCd: String, salesOrgCd: String, storCd: String, cornerCd: String): Resource<List<OrderStatus?>>
}