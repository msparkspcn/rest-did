package com.secta9ine.rest.did.data.local.dao

import android.database.sqlite.SQLiteException
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.secta9ine.rest.did.domain.model.OrderStatus
import kotlinx.coroutines.flow.Flow
import kotlin.jvm.Throws

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Throws(SQLiteException::class)
    suspend fun insert(orderStatus: OrderStatus)

    @Query(
        """
            SELECT CMP_CD, SALES_ORG_CD, STOR_CD, CORNER_CD, SALE_DT, TRADE_NO, POS_NO, STATUS, ORDER_NO_C, COM_TIME, REG_DATE
            FROM ORDER_STATUS
            WHERE 1 = 1
                AND SALE_DT = :saleDt
                AND CMP_CD = :cmpCd
                AND SALES_ORG_CD = :salesOrgCd
                AND STOR_CD = :storCd
                AND CORNER_CD = :cornerCd
                ORDER BY COM_TIME, REG_DATE
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
            SET STATUS = :status
            WHERE 1 = 1
            AND SALE_DT = :saleDt
                AND CMP_CD = :cmpCd
                AND SALES_ORG_CD = :salesOrgCd
                AND STOR_CD = :storCd
                AND CORNER_CD = :cornerCd
                AND TRADE_NO = :tradeNo
                AND POS_NO = :posNo
            
        """
    )
    fun updateOrderStatus(saleDt: String, cmpCd: String, salesOrgCd: String, storCd: String,
                          cornerCd: String, tradeNo: String, posNo: String, status: String)

}