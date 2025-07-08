package com.secta9ine.rest.did.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.secta9ine.rest.did.domain.model.OrderStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderStatusDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(orderStatus: List<OrderStatus>)

    @Query("DELETE FROM ORDER_STATUS")
    suspend fun deleteAll()

    @Transaction
    suspend fun sync(list: List<OrderStatus>) {
        deleteAll()
        insertAll(list)
    }

    @Query(
        """
            SELECT CMP_CD, SALES_ORG_CD, STOR_CD, CORNER_CD, SALE_DT, ORDER_NO, ORDER_STATUS, ORDER_NO_C
            FROM ORDER_STATUS
            WHERE 1 = 1
                AND SALE_DT = :saleDt
                AND CMP_CD = :cmpCd
                AND SALES_ORG_CD = :salesOrgCd
                AND STOR_CD = :storCd
                AND CORNER_CD = :cornerCd
                ORDER BY COM_TIME
        """
    )
    fun get(saleDt: String, cmpCd: String, salesOrgCd: String, storCd: String, cornerCd: String): Flow<List<OrderStatus?>>

    @Query(
        """
            SELECT *
            FROM ORDER_STATUS
            WHERE 1 = 1
                AND SALE_DT = :saleDt
                AND CMP_CD = :cmpCd
                AND SALES_ORG_CD = :salesOrgCd
                AND STOR_CD = :storCd
                AND CORNER_CD = :cornerCd
                AND ORDER_NO_C = :orderNoC
        """
    )
    fun getByOrderNoC(saleDt: String, cmpCd: String, salesOrgCd: String, storCd: String, cornerCd: String, orderNoC: String): Flow<OrderStatus?>

    @Query(
        """
            UPDATE ORDER_STATUS
            SET ORDER_STATUS = :orderStatus
            WHERE 1 = 1
            AND SALE_DT = :saleDt
                AND CMP_CD = :cmpCd
                AND SALES_ORG_CD = :salesOrgCd
                AND STOR_CD = :storCd
                AND CORNER_CD = :cornerCd
                AND ORDER_NO = :orderNo
            
        """
    )
    fun updateOrderStatus(saleDt: String, cmpCd: String, salesOrgCd: String, storCd: String,
                          cornerCd: String, orderNo: String, orderStatus: String)

}