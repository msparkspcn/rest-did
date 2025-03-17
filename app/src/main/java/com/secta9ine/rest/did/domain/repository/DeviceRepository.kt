package com.secta9ine.rest.did.domain.repository

import com.secta9ine.rest.did.domain.model.Device
import kotlinx.coroutines.flow.Flow

interface DeviceRepository {
    suspend fun sync(device: Device)
    suspend fun getDevice(deviceId: String): Flow<Device>

}