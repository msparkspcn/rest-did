package com.secta9ine.rest.did.data.local.repository

import com.secta9ine.rest.did.data.local.dao.DeviceDao
import com.secta9ine.rest.did.data.local.dao.OrderStatusDao
import com.secta9ine.rest.did.domain.model.Device
import com.secta9ine.rest.did.domain.model.OrderStatus
import com.secta9ine.rest.did.domain.repository.DeviceRepository
import com.secta9ine.rest.did.domain.repository.OrderStatusRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrderStatusRepositoryImpl @Inject constructor(
    private val orderStatusDao: OrderStatusDao
) : OrderStatusRepository {
    override suspend fun sync(orderStatusList: List<OrderStatus>) {
        orderStatusDao.sync(orderStatusList)
    }

    override suspend fun get(
        saleDt: String, cmpCd: String, salesOrgCd: String, storCd: String, cornerCd: String
    ): Flow<List<OrderStatus?>> =
        orderStatusDao.get(
            saleDt = saleDt,
            cmpCd = cmpCd,
            salesOrgCd = salesOrgCd,
            storCd = storCd,
            cornerCd = cornerCd
        )

    override suspend fun updateOrderStatus(saleDt: String, cmpCd: String, salesOrgCd: String, storCd: String, cornerCd: String, orderNo: String, orderStatus: String) {
        orderStatusDao.updateOrderStatus(
            saleDt = saleDt,
            cmpCd = cmpCd,
            salesOrgCd = salesOrgCd,
            storCd = storCd,
            cornerCd = cornerCd,
            orderNo = orderNo,
            orderStatus = orderStatus
        )
    }
}