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
}