package com.secta9ine.rest.did.data.local.repository

import com.secta9ine.rest.did.data.local.dao.DeviceDao
import com.secta9ine.rest.did.domain.model.Device
import com.secta9ine.rest.did.domain.repository.DeviceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeviceRepositoryImpl @Inject constructor(
    private val deviceDao: DeviceDao
) : DeviceRepository {
    override suspend fun sync(device: Device) {
        deviceDao.sync(device)
    }

    override suspend fun getDevice(deviceId: String): Flow<Device> =
        deviceDao.get(
            deviceId = deviceId
        )
}