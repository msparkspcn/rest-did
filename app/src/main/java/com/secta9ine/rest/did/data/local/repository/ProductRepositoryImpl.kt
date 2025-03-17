package com.secta9ine.rest.did.data.local.repository

import com.secta9ine.rest.did.data.local.dao.DeviceDao
import com.secta9ine.rest.did.data.local.dao.ProductDao
import com.secta9ine.rest.did.domain.model.Device
import com.secta9ine.rest.did.domain.model.Product
import com.secta9ine.rest.did.domain.repository.DeviceRepository
import com.secta9ine.rest.did.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao
) : ProductRepository {
    override suspend fun sync(productList: List<Product>) {
        productDao.sync(productList)
    }

}