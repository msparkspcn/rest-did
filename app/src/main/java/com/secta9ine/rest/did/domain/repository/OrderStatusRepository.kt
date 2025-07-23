package com.secta9ine.rest.did.domain.repository

import com.secta9ine.rest.did.domain.model.OrderStatus
import kotlinx.coroutines.flow.Flow

interface OrderStatusRepository {
    suspend fun sync(orderStatusList: List<OrderStatus>)
    suspend fun insertAll(orderStatusList: List<OrderStatus>)
    suspend fun insert(orderStatus: OrderStatus)
    suspend fun getCnt(saleDt: String, cmpCd: String, salesOrgCd: String, storCd: String,
                       cornerCd: String, tradeNo: String, posNo: String): Int
    suspend fun get(saleDt: String, cmpCd: String, salesOrgCd: String, storCd: String, cornerCd: String): Flow<List<OrderStatus?>>
    suspend fun getByOrderNoC(saleDt: String, cmpCd: String, salesOrgCd: String, storCd: String, cornerCd: String, orderNoC: String): Flow<OrderStatus?>
    suspend fun updateOrderStatus(saleDt: String, cmpCd: String, salesOrgCd: String, storCd: String,
                                  cornerCd: String, tradeNo: String, posNo: String, status: String)
    suspend fun updateOrderCallStatus(saleDt: String, cmpCd: String, salesOrgCd: String, storCd: String, cornerCd: String)
}