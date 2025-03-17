package com.secta9ine.rest.did.domain.repository

import com.secta9ine.rest.did.domain.model.OrderStatus

interface OrderStatusRepository {
    suspend fun sync(orderStatusList: List<OrderStatus>)
}