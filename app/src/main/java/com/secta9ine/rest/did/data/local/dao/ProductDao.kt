package com.secta9ine.rest.did.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.secta9ine.rest.did.domain.model.Product

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<Product>)

    @Query("DELETE FROM PRODUCT")
    suspend fun deleteAll()

    @Transaction
    suspend fun sync(list: List<Product>) {
        deleteAll()
        insertAll(list)
    }
}