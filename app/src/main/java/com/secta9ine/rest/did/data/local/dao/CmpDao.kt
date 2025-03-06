package com.secta9ine.rest.did.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.secta9ine.rest.did.domain.model.Cmp

@Dao
interface CmpDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<Cmp>)

    @Query("DELETE FROM MST_CMP")
    suspend fun deleteAll()

    @Transaction
    suspend fun sync(list: List<Cmp>) {
        deleteAll()
        insertAll(list)
    }
}