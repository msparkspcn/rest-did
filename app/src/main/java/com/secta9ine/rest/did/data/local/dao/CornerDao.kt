package com.secta9ine.rest.did.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.secta9ine.rest.did.domain.model.Corner

@Dao
interface CornerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cornerList: List<Corner>)
}