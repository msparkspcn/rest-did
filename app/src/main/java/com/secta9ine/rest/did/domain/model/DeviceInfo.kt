package com.secta9ine.rest.did.domain.model

data class DeviceInfo(
    val device: Device = Device(),
    val productList: List<Product> = emptyList(),
    val orderStatusList: List<OrderStatus> = emptyList()
)