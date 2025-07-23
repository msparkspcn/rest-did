package com.secta9ine.rest.did.data.local.repository

import com.secta9ine.rest.did.data.local.dao.ProductDao
import com.secta9ine.rest.did.domain.model.Product
import com.secta9ine.rest.did.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao
) : ProductRepository {
    override suspend fun sync(productList: List<Product>) {
        productDao.sync(productList)
    }

    override suspend fun getProductList(cmpCd: String, salesOrgCd: String, storCd: String, cornerCd: String): Flow<List<Product>> =
        productDao.get(
            cmpCd = cmpCd,
            salesOrgCd = salesOrgCd,
            storCd = storCd,
            cornerCd = cornerCd
        )

    override suspend fun updateSoldoutYn(cmpCd: String, salesOrgCd: String, storCd: String, itemCd: String, soldoutYn: String) =
        productDao.updateSoldoutYn(
            cmpCd = cmpCd,
            salesOrgCd = salesOrgCd,
            storCd = storCd,
            itemCd = itemCd,
            soldoutYn = soldoutYn
        )



}