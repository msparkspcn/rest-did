package com.secta9ine.rest.did.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.secta9ine.rest.did.domain.model.SaleOpen
import kotlinx.coroutines.flow.Flow
@Dao
interface SaleOpenDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(saleOpen: SaleOpen)

    @Query(
        """
            SELECT *
            FROM SALE_OPEN
            WHERE 1 = 1
            AND CMP_CD = :cmpCd
            AND SALES_ORG_CD = :salesOrgCd
            AND STOR_CD = :storCd
            ORDER BY SALE_DT DESC
            LIMIT 1
        """
    )
    fun get(cmpCd: String, salesOrgCd: String, storCd: String): Flow<SaleOpen?>
}