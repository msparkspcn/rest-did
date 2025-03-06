package com.secta9ine.rest.did.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "PRODUCT_DETAIL",
    primaryKeys = ["PRODUCT_CD"]
)
data class ProductDetail(
    @ColumnInfo(name = "SEQ") val seq: Long? = null, // SEQ 번호
    @ColumnInfo(name = "PRODUCT_CD") val productCd: String = "",
    @ColumnInfo(name = "SORT_ORDER") val sortOrder: String? = null,
    @ColumnInfo(name = "DID_YN") val didYn: String? =null,
    @ColumnInfo(name = "SOLD_OUT_YN") val soldOutYn: String? =null
)