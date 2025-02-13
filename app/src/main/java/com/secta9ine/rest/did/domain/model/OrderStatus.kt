package com.secta9ine.rest.did.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "ORDER_STATUS",
    primaryKeys = ["SALE_DT", "STORE_CD", "ORDER_NO"]
)
data class OrderStatus (
    @ColumnInfo(name = "SEQ") val seq: Long? = null, // SEQ 번호
    @ColumnInfo(name = "SALE_DT") val saleDt: String = "",
    @ColumnInfo(name = "STORE_CD") val storeCd: String = "",
    @ColumnInfo(name = "ORDER_NO") val orderNo: String = "",
    @ColumnInfo(name = "ORDER_STATUS") val orderStatus: String = "",    //주문상태(준비완료, 준비중)

)