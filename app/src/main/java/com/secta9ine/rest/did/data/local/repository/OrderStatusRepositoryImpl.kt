package com.secta9ine.rest.did.data.local.repository

import com.secta9ine.rest.did.data.local.dao.OrderStatusDao
import com.secta9ine.rest.did.domain.model.OrderStatus
import com.secta9ine.rest.did.domain.repository.OrderStatusRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrderStatusRepositoryImpl @Inject constructor(
    private val orderStatusDao: OrderStatusDao
) : OrderStatusRepository {
    override suspend fun sync(orderStatusList: List<OrderStatus>) {
        orderStatusDao.sync(orderStatusList)
    }

    override suspend fun insert(orderStatus: OrderStatus) {
        orderStatusDao.insert(orderStatus)
    }

    override suspend fun getCnt(saleDt: String, cmpCd: String, salesOrgCd: String, storCd: String, cornerCd: String, tradeNo: String, posNo: String):Int {
        return orderStatusDao.getOrderStatusCnt(
            saleDt = saleDt,
            cmpCd = cmpCd,
            salesOrgCd = salesOrgCd,
            storCd = storCd,
            cornerCd = cornerCd,
            tradeNo = tradeNo,
            posNo = posNo
        )
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

    override suspend fun getByOrderNoC(saleDt: String, cmpCd: String, salesOrgCd: String, storCd: String, cornerCd: String, orderNoC: String)
    : Flow<OrderStatus?> =
        orderStatusDao.getByOrderNoC(
            saleDt = saleDt,
            cmpCd = cmpCd,
            salesOrgCd = salesOrgCd,
            storCd = storCd,
            cornerCd = cornerCd,
            orderNoC = orderNoC
        )


    override suspend fun updateOrderStatus(
        saleDt: String,
        cmpCd: String,
        salesOrgCd: String,
        storCd: String,
        cornerCd: String,
        tradeNo: String,
        posNo: String,
        status: String) {
        orderStatusDao.updateOrderStatus(
            saleDt = saleDt,
            cmpCd = cmpCd,
            salesOrgCd = salesOrgCd,
            storCd = storCd,
            cornerCd = cornerCd,
            tradeNo = tradeNo,
            posNo = posNo,
            status = status
        )
    }
}