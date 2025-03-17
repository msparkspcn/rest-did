package com.secta9ine.rest.did.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.secta9ine.rest.did.domain.model.OrderStatus

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
}