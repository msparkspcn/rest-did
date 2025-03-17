package com.secta9ine.rest.did.domain.repository

import com.secta9ine.rest.did.domain.model.Product

interface ProductRepository {
    suspend fun sync(productList: List<Product>)
}