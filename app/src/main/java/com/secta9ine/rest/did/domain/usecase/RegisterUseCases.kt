package com.secta9ine.rest.did.domain.usecase

import android.app.Application
import android.util.Log
import com.secta9ine.rest.did.R
import com.secta9ine.rest.did.data.local.database.AppDatabase
import com.secta9ine.rest.did.domain.model.Corner
import com.secta9ine.rest.did.domain.model.Device
import com.secta9ine.rest.did.domain.model.DeviceInfo
import com.secta9ine.rest.did.domain.model.Product
import com.secta9ine.rest.did.domain.repository.CornerRepository
import com.secta9ine.rest.did.domain.repository.DeviceRepository
import com.secta9ine.rest.did.domain.repository.OrderStatusRepository
import com.secta9ine.rest.did.domain.repository.ProductRepository
import com.secta9ine.rest.did.domain.repository.RestApiRepository
import com.secta9ine.rest.did.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RegisterUseCases @Inject constructor(
    private val app: Application,
    private val database: AppDatabase,
    private val restApiRepository: RestApiRepository,
    private val productRepository: ProductRepository,
    private val orderStatusRepository: OrderStatusRepository,
    private val deviceRepository: DeviceRepository,
    private val cornerRepository: CornerRepository

) {
    private val tag = this.javaClass.simpleName
    suspend fun fetch(device: Device): Resource<DeviceInfo> =
        withContext(Dispatchers.IO) {
            Log.d(tag,"device:$device")

            // 필수 필드 체크
            if (device.cmpCd == null || device.salesOrgCd == null ||
                device.storCd == null || device.cornerCd == null) {
                return@withContext Resource.Failure(app.resources.getString(R.string.no_device_config_error))
            }
            val results = listOf(
                async {
                    restApiRepository.getProductList(
                        device.cmpCd!!,
                        device.salesOrgCd!!,
                        device.storCd!!,
                        device.cornerCd!!
                    )
                },
                async {
                    restApiRepository.getCornerInfo(
                        device.cmpCd!!,
                        device.salesOrgCd!!,
                        device.storCd!!,
                        device.cornerCd!!
                    )
                },
            )
                .awaitAll()
                .also {
                    it.firstOrNull { it is Resource.Failure }?.let {
                        return@withContext Resource.Failure(it.message!!)
                    }
                    it.firstOrNull { it.data == null }?.let {
                        return@withContext Resource.Failure(app.resources.getString(R.string.no_device_config_error))
                    }
                }
            Resource.Success(
                DeviceInfo(
                    device = device,
                    productList = results[0].data!! as List<Product>,
                    corner = results[1].data!! as Corner,
                )
            )
        }

    /*
    suspend fun register(deviceInfo: DeviceInfo) = withContext(Dispatchers.IO) {
        database.clearAllTables()

        deviceRepository.sync(deviceInfo.device)

//        productRepository.sync(deviceInfo.productList)
//        orderStatusRepository.sync(deviceInfo.orderStatusList)
    }
     */

    //temp
    suspend fun register(deviceInfo: DeviceInfo) = withContext(Dispatchers.IO) {
        productRepository.sync(deviceInfo.productList)
        deviceRepository.sync(deviceInfo.device)
        cornerRepository.insert(deviceInfo.corner)
    }
}