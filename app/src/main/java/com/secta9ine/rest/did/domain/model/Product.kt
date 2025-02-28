package com.secta9ine.rest.did.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "PRODUCT",
    primaryKeys = ["PRODUCT_CD"]
)
data class Product(
    @ColumnInfo(name = "SEQ") val seq: Long? = null, // SEQ 번호
    @ColumnInfo(name = "PRODUCT_CD") val productCd: String = "",
    @ColumnInfo(name = "PRODUCT_NM") val productNm: String = "",
    @ColumnInfo(name = "ENGPRODUCT_NM") val productEngNm: String? =null,
    @ColumnInfo(name = "PRICE") val price: Int = 0,
    @ColumnInfo(name = "TAG") val tag: String? = null,
    @ColumnInfo(name = "IMG_PATH") val imgPath: String? =null
)