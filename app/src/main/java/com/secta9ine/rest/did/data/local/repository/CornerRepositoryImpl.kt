package com.secta9ine.rest.did.data.local.repository

import com.secta9ine.rest.did.data.local.dao.CornerDao
import com.secta9ine.rest.did.domain.model.Corner
import com.secta9ine.rest.did.domain.repository.CornerRepository
import javax.inject.Inject

class CornerRepositoryImpl @Inject constructor(
    private val cornerDao: CornerDao
) : CornerRepository {
    override suspend fun insert(corner: Corner) {
        cornerDao.insert(corner)
    }

}