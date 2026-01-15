package com.secta9ine.rest.did.domain.repository

import com.secta9ine.rest.did.domain.model.Product
import com.secta9ine.rest.did.domain.model.ProductVo
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun sync(productList: List<Product>)
    suspend fun getProductList(cmpCd: String, salesOrgCd: String, storCd: String, corners: Set<String>): Flow<List<ProductVo>>
    suspend fun updateSoldoutYn(cmpCd: String, salesOrgCd: String, storCd: String, itemCd: String, soldoutYn: String)
    suspend fun getProduct(cmpCd: String, salesOrgCd: String, storCd: String, itemCd: String): Flow<List<ProductVo>>

}