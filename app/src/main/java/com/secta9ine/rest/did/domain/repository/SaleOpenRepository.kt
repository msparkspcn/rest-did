package com.secta9ine.rest.did.domain.repository

import com.secta9ine.rest.did.domain.model.SaleOpen
import kotlinx.coroutines.flow.Flow
interface SaleOpenRepository {
    suspend fun insert(saleOpen: SaleOpen)

    suspend fun get(cmpCd: String, salesOrgCd: String, storCd: String): Flow<SaleOpen?>
}