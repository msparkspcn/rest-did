package com.secta9ine.rest.did.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.secta9ine.rest.did.domain.model.Product
import com.secta9ine.rest.did.domain.model.ProductVo
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
            SELECT P.CMP_CD, P.SALES_ORG_CD, P.STOR_CD, P.CORNER_CD, C.CORNER_NM, P.ITEM_CD, 
            P.ITEM_NM, P.ITEM_NM_EN, P.PRICE, P.TAG, P.IMG_PATH, P.SOLDOUT_YN, P.WEEK_DIV,
            P.SALE_CLOSE_START_TIME, P.SALE_CLOSE_END_TIME, P.SORT_ORDER, P.USE_YN,
            P.PRODUCT_EXPLN, P.CALORY
            FROM PRODUCT P JOIN MST_CORNER C
            WHERE 1 = 1
                AND P.CMP_CD = C.CMP_CD
                AND P.SALES_ORG_CD = C.SALES_ORG_CD
                AND P.STOR_CD = C.STOR_CD
                AND P.CORNER_CD = C.CORNER_CD
                AND P.CMP_CD = :cmpCd
                AND P.SALES_ORG_CD = :salesOrgCd
                AND P.STOR_CD = :storCd
                AND P.CORNER_CD IN (:cornerCds)
                AND P.USE_YN = '1'
                AND P.SOLDOUT_YN = '0'
            ORDER BY SORT_ORDER
        """,
    )
    fun get(cmpCd: String, salesOrgCd: String, storCd: String, cornerCds: Set<String>): Flow<List<ProductVo>>

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