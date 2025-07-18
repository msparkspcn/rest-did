package com.secta9ine.rest.did.domain.usecase

import android.app.Application
import android.util.Log
import com.secta9ine.rest.did.R
import com.secta9ine.rest.did.data.local.database.AppDatabase
import com.secta9ine.rest.did.domain.model.Device
import com.secta9ine.rest.did.domain.model.DeviceInfo
import com.secta9ine.rest.did.domain.model.OrderStatus
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
//                async {
//                    restApiRepository.getOrderList(
//                        device.cmpCd,
//                        device.salesOrgCd,
//                        device.storCd,
//                        device.cornerCd
//                    )
//                },
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
                    productList = results[0].data!!,
//                    orderStatusList = results[1].data!! as List<OrderStatus>,
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
        database.clearAllTables()
        productRepository.sync(deviceInfo.productList)
        deviceRepository.sync(deviceInfo.device)
        val initOrderList: List<OrderStatus> =
            listOf(
                OrderStatus(
                    saleDt = "20250707",
                    cmpCd = "SLKR",
                    salesOrgCd = "8000",
                    storCd = "5000511",
                    cornerCd = "CIHA",
                    tradeNo = "21454",
                    posNo = "22",
                    status = "2",
                    orderNoC = "21454",
                    ordTime = "", comTime = ""),
                OrderStatus(
                    saleDt = "20250707",
                    cmpCd = "SLKR",
                    salesOrgCd = "8000",
                    storCd = "5000511",
                    cornerCd = "CIHA",
                    tradeNo = "21460",
                    posNo = "22",
                    status = "2",
                    orderNoC = "21460",
                    ordTime = "", comTime = ""),
                OrderStatus(
                    saleDt = "20250707",
                    cmpCd = "SLKR",
                    salesOrgCd = "8000",
                    storCd = "5000511",
                    cornerCd = "CIHA",
                    tradeNo = "21461",
                    posNo = "22",
                    status = "2",
                    orderNoC = "21461",
                    ordTime = "", comTime = ""),
                OrderStatus(
                    saleDt = "20250707",
                    cmpCd = "SLKR",
                    salesOrgCd = "8000",
                    storCd = "5000511",
                    cornerCd = "CIHA",
                    tradeNo = "21462",
                    posNo = "22",
                    status = "2",
                    orderNoC = "21462",
                    ordTime = "", comTime = ""),
                OrderStatus(
                    saleDt = "20250707",
                    cmpCd = "SLKR",
                    salesOrgCd = "8000",
                    storCd = "5000511",
                    cornerCd = "CIHA",
                    tradeNo = "21463",
                    posNo = "22",
                    status = "2",
                    orderNoC = "21463",
                    ordTime = "", comTime = ""),
                OrderStatus(
                    saleDt = "20250707",
                    cmpCd = "SLKR",
                    salesOrgCd = "8000",
                    storCd = "5000511",
                    cornerCd = "CIHA",
                    tradeNo = "21464",
                    posNo = "22",
                    status = "2",
                    orderNoC = "21464",
                    ordTime = "", comTime = ""),
                OrderStatus(
                    saleDt = "20250707",
                    cmpCd = "SLKR",
                    salesOrgCd = "8000",
                    storCd = "5000511",
                    cornerCd = "CIHA",
                    tradeNo = "21465",
                    posNo = "22",
                    status = "2",
                    orderNoC = "21465",
                    ordTime = "", comTime = ""),
                OrderStatus(
                    saleDt = "20250707",
                    cmpCd = "SLKR",
                    salesOrgCd = "8000",
                    storCd = "5000511",
                    cornerCd = "CIHA",
                    tradeNo = "21466",
                    posNo = "22",
                    status = "2",
                    orderNoC = "21466",
                    ordTime = "", comTime = ""),
//                OrderStatus(
//                    seq = 17,
//                    saleDt = "20250707",
//                    cmpCd = "SLKR",
//                    salesOrgCd = "8000",
//                    storCd = "5000511",
//                    cornerCd = "CIHA",
//                    tradeNo = "21467",
//                    status = "2",
//                    orderNoC = "21467",
//                    ordTime = "", comTime = ""),
//                OrderStatus(
//                    seq = 18,
//                    saleDt = "20250707",
//                    cmpCd = "SLKR",
//                    salesOrgCd = "8000",
//                    storCd = "5000511",
//                    cornerCd = "CIHA",
//                    tradeNo = "21468",
//                    status = "2",
//                    orderNoC = "21468",
//                    ordTime = "", comTime = ""),
//                OrderStatus(
//                    seq = 19,
//                    saleDt = "20250707",
//                    cmpCd = "SLKR",
//                    salesOrgCd = "8000",
//                    storCd = "5000511",
//                    cornerCd = "CIHA",
//                    tradeNo = "21469",
//                    status = "2",
//                    orderNoC = "21469",
//                    ordTime = "", comTime = ""),
                OrderStatus(
                    saleDt = "20250707",
                    cmpCd = "SLKR",
                    salesOrgCd = "8000",
                    storCd = "5000511",
                    cornerCd = "CIHA",
                    tradeNo = "21455",
                    posNo = "22",
                    status = "C",
                    orderNoC = "21455",
                    ordTime = "", comTime = ""),
                OrderStatus(
                    saleDt = "20250707",
                    cmpCd = "SLKR",
                    salesOrgCd = "8000",
                    storCd = "5000511",
                    cornerCd = "CIHA",
                    tradeNo = "21456",
                    posNo = "22",
                    status = "4",
                    orderNoC = "21456",
                    ordTime = "162000", comTime = "163000"),
                OrderStatus(
                    saleDt = "20250707",
                    cmpCd = "SLKR",
                    salesOrgCd = "8000",
                    storCd = "5000511",
                    cornerCd = "CIHA",
                    tradeNo = "21476",
                    posNo = "22",
                    status = "4",
                    orderNoC = "21476",
                    ordTime = "162005", comTime = "163005"),
                OrderStatus(
                    saleDt = "20250707",
                    cmpCd = "SLKR",
                    salesOrgCd = "8000",
                    storCd = "5000511",
                    cornerCd = "CIHA",
                    tradeNo = "21477",
                    posNo = "22",
                    status = "4",
                    orderNoC = "21477",
                    ordTime = "162010", comTime = "162510"),
                OrderStatus(
                    saleDt = "20250707",
                    cmpCd = "SLKR",
                    salesOrgCd = "8000",
                    storCd = "5000511",
                    cornerCd = "CIHA",
                    tradeNo = "21478",
                    posNo = "22",
                    status = "4",
                    orderNoC = "21478",
                    ordTime = "162015", comTime = "163015"),
//                OrderStatus(
//                    seq = 23,
//                    saleDt = "20250707",
//                    cmpCd = "SLKR",
//                    salesOrgCd = "8000",
//                    storCd = "5000511",
//                    cornerCd = "CIHA",
//                    tradeNo = "21479",
//                    status = "4",
//                    orderNoC = "21479",
//                    ordTime = "162020", comTime = "163020"),
//                OrderStatus(
//                    seq = 24,
//                    saleDt = "20250707",
//                    cmpCd = "SLKR",
//                    salesOrgCd = "8000",
//                    storCd = "5000511",
//                    cornerCd = "CIHA",
//                    tradeNo = "21480",
//                    status = "4",
//                    orderNoC = "21480",
//                    ordTime = "162025", comTime = "163025"),
                OrderStatus(
                    saleDt = "20250707",
                    cmpCd = "SLKR",
                    salesOrgCd = "8000",
                    storCd = "5000511",
                    cornerCd = "CIHA",
                    tradeNo = "21457",
                    posNo = "22",
                    status = "3",
                    orderNoC = "21457",
                    ordTime = "", comTime = ""),
                OrderStatus(
                    saleDt = "20250707",
                    cmpCd = "SLKR",
                    salesOrgCd = "8000",
                    storCd = "5000511",
                    cornerCd = "CIHA",
                    tradeNo = "21458",
                    posNo = "22",
                    status = "4",
                    orderNoC = "21458",
                    ordTime = "", comTime = "")
            )

        orderStatusRepository.sync(initOrderList)

    }
}