package com.secta9ine.rest.did.domain.repository

import com.secta9ine.rest.did.domain.model.Store
import com.secta9ine.rest.did.domain.model.User
import com.secta9ine.rest.did.util.Resource

interface RestApiRepository {
    suspend fun getStoreInfo(storeCd: String, storePassword: String): Resource<Store?>
    suspend fun acceptLogin(userId: String, password: String): Resource<User?>
}