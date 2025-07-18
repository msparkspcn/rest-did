package com.secta9ine.rest.did.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "ORDER_STATUS",
    primaryKeys = ["SALE_DT", "CMP_CD", "SALES_ORG_CD", "STOR_CD", "CORNER_CD", "POS_NO", "TRADE_NO"]
)
data class OrderStatus (
    @ColumnInfo(name = "SALE_DT") val saleDt: String = "",
    @ColumnInfo(name = "CMP_CD") val cmpCd: String = "",
    @ColumnInfo(name = "SALES_ORG_CD") val salesOrgCd: String = "",
    @ColumnInfo(name = "STOR_CD") val storCd: String = "",
    @ColumnInfo(name = "CORNER_CD") val cornerCd: String = "",
    @ColumnInfo(name = "POS_NO") val posNo: String = "",
    @ColumnInfo(name = "TRADE_NO") val tradeNo: String = "",
    @ColumnInfo(name = "STATUS") val status: String = "",    //주문 상태(준비 완료, 준비중)
    @ColumnInfo(name = "ORD_TIME") val ordTime: String? = null,
    @ColumnInfo(name = "COM_TIME") val comTime: String? = null,
    @ColumnInfo(name = "ORDER_NO_C") val orderNoC: String = "",
    @ColumnInfo(name = "REG_DATE") val regDate: String? = null,
    @ColumnInfo(name = "UPD_DATE") val updDate: String? = null,
)