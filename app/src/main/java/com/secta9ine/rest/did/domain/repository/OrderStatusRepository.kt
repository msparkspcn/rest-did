package com.secta9ine.rest.did.domain.repository

import com.secta9ine.rest.did.domain.model.OrderStatus
import kotlinx.coroutines.flow.Flow

interface OrderStatusRepository {
    suspend fun sync(orderStatusList: List<OrderStatus>)

    suspend fun get(saleDt: String, cmpCd: String, salesOrgCd: String, storCd: String, cornerCd: String): Flow<List<OrderStatus?>>
    suspend fun getByOrderNoC(saleDt: String, cmpCd: String, salesOrgCd: String, storCd: String, cornerCd: String, orderNoC: String): Flow<OrderStatus?>
    suspend fun updateOrderStatus(saleDt: String, cmpCd: String, salesOrgCd: String, storCd: String,
                                  cornerCd: String, orderNo: String, orderStatus: String)
}