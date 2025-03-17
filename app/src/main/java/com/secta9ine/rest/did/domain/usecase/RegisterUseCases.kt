package com.secta9ine.rest.did.domain.usecase

import android.app.Application
import android.util.Log
import com.secta9ine.rest.did.R
import com.secta9ine.rest.did.data.local.database.AppDatabase
import com.secta9ine.rest.did.domain.model.DeviceInfo
import com.secta9ine.rest.did.domain.model.OrderStatus
import com.secta9ine.rest.did.domain.model.Product
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
    private val deviceRepository: DeviceRepository

) {
    private val tag = this.javaClass.simpleName
    suspend fun fetch(deviceId: String): Resource<DeviceInfo> =
        withContext(Dispatchers.IO) {
            restApiRepository.getDevice(deviceId).let { //device 조회 하는 api 로 교체
                when(it) {
                    is Resource.Success -> {
                        val device = it.data
                            ?:return@withContext Resource.Failure("")

                        Log.d(tag,"device:$device")

                        // 필수 필드 체크
                        if (device.cmpCd == null || device.salesOrgCd == null ||
                            device.storCd == null || device.cornerCd == null) {
                            return@withContext Resource.Failure(app.resources.getString(R.string.no_device_config_error))
                        }
                        val results = listOf(
                            async {
                                restApiRepository.getProductList(
                                    device.cmpCd,
                                    device.salesOrgCd,
                                    device.storCd,
                                    device.cornerCd
                                )
                            },
                            async {
                                restApiRepository.getOrderList(
                                    device.cmpCd,
                                    device.salesOrgCd,
                                    device.storCd,
                                    device.cornerCd
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
                                orderStatusList = results[1].data!! as List<OrderStatus>,
                            )
                        )
                    }
                    is Resource.Failure -> {
                        Resource.Failure(it.message!!)
                    }
                }
            }
        }

    suspend fun register(deviceInfo: DeviceInfo) = withContext(Dispatchers.IO) {
        database.clearAllTables()

        deviceRepository.sync(deviceInfo.device)

        productRepository.sync(deviceInfo.productList)
        orderStatusRepository.sync(deviceInfo.orderStatusList)
    }
}