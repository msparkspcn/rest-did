package com.secta9ine.rest.did.data.local.repository

import com.secta9ine.rest.did.data.local.dao.SaleOpenDao
import com.secta9ine.rest.did.domain.model.SaleOpen
import com.secta9ine.rest.did.domain.repository.SaleOpenRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaleOpenRepositoryImpl @Inject constructor(
    private val saleOpenDao: SaleOpenDao
) : SaleOpenRepository {
    override suspend fun insert(saleOpen: SaleOpen) {
        saleOpenDao.insert(saleOpen)
    }

    override suspend fun get(cmpCd: String, salesOrgCd: String, storCd: String): Flow<SaleOpen?> =
        saleOpenDao.get(
            cmpCd = cmpCd,
            salesOrgCd = salesOrgCd,
            storCd = storCd
        )
}