package com.secta9ine.rest.did.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.secta9ine.rest.did.domain.model.Product
import kotlinx.coroutines.flow.Flow

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

    @Query(
        """
            SELECT CMP_CD, SALES_ORG_CD, STOR_CD, CORNER_CD, ITEM_CD, 
            ITEM_NM, ITEM_NM_EN, PRICE, TAG, IMG_PATH, SOLDOUT_YN, WEEK_DIV,
            SALE_CLOSE_START_TIME, SALE_CLOSE_END_TIME, SORT_ORDER, USE_YN,
            PRODUCT_EXPLN, CALORY
            FROM PRODUCT
            WHERE 1 = 1
                AND CMP_CD = :cmpCd
                AND SALES_ORG_CD = :salesOrgCd
                AND STOR_CD = :storCd
                AND CORNER_CD = :cornerCd
                AND USE_YN = '1'
                AND SOLDOUT_YN = '0'
        """
    )
    fun get(cmpCd: String, salesOrgCd: String, storCd: String, cornerCd: String): Flow<List<Product>>

    @Query(
        """
            UPDATE PRODUCT
            SET SOLDOUT_YN = :soldoutYn
            WHERE 1 = 1
            AND CMP_CD = :cmpCd
            AND SALES_ORG_CD = :salesOrgCd
            AND STOR_CD = :storCd
            AND ITEM_CD = :itemCd
        """
    )
    fun updateSoldoutYn(cmpCd: String, salesOrgCd: String, storCd: String, itemCd: String, soldoutYn: String)
}