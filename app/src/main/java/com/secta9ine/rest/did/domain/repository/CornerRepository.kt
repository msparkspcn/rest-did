package com.secta9ine.rest.did.domain.repository

import com.secta9ine.rest.did.domain.model.Corner

interface CornerRepository {
    suspend fun insert(corner: Corner)
}