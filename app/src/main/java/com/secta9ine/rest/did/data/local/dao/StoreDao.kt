package com.secta9ine.rest.did.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.secta9ine.rest.did.domain.model.Store
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(store: Store)

    @Query("DELETE FROM STORE")
    suspend fun deleteAll()

    @Transaction
    suspend fun sync(store: Store) {
        deleteAll()
        insert(store)
    }

    @Query(
        """
            SELECT * 
            FROM STORE
            WHERE 1 = 1
                AND STORE_CD = :storeCd
        """
    )
    fun get(storeCd: String): Flow<Store?>
}