package com.secta9ine.rest.did.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.secta9ine.rest.did.domain.model.Stor
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stor: Stor)

    @Query("DELETE FROM MST_STOR")
    suspend fun deleteAll()

    @Transaction
    suspend fun sync(stor: Stor) {
        deleteAll()
        insert(stor)
    }

    @Query(
        """
            SELECT * 
            FROM MST_STOR
            WHERE 1 = 1
                AND STOR_CD = :storCd
        """
    )
    fun get(storCd: String): Flow<Stor?>
}