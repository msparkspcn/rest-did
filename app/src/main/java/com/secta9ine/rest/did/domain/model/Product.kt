package com.secta9ine.rest.did.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "PRODUCT",
    primaryKeys = ["CMP_CD","SALES_ORG_CD","STOR_CD","CORNER_CD","ITEM_CD"]
)
data class Product(
    @ColumnInfo(name = "SEQ") val seq: Long? = null, // SEQ 번호
    @ColumnInfo(name = "CMP_CD") val cmpCd: String = "",
    @ColumnInfo(name = "SALES_ORG_CD") val salesOrgCd: String = "",
    @ColumnInfo(name = "STOR_CD") val storCd: String = "",
    @ColumnInfo(name = "CORNER_CD") val cornerCd: String = "",
    @ColumnInfo(name = "ITEM_CD") val itemCd: String = "",
    @ColumnInfo(name = "ITEM_NM") val itemNm: String = "",
    @ColumnInfo(name = "ITEM_NM_EN") val itemNmEn: String? =null,
    @ColumnInfo(name = "PRICE") val price: Int = 0,
    @ColumnInfo(name = "TAG") val tag: String? = null,
    @ColumnInfo(name = "IMG_PATH") val imgPath: String? =null,
    @ColumnInfo(name = "SOLDOUT_YN") val soldoutYn: String? =null,
    @ColumnInfo(name = "WEEK_DIV") val weekDiv: String ="",
    @ColumnInfo(name = "SALE_CLOSE_START_TIME") val saleCloseStartTime: String? =null,
    @ColumnInfo(name = "SALE_CLOSE_END_TIME") val saleCloseEndTime: String? =null,
    @ColumnInfo(name = "SORT_ORDER") val sortOrder: Int = 0,
    @ColumnInfo(name = "USE_YN") val useYn: String? =null,
    @ColumnInfo(name = "PRODUCT_EXPLN") val productExpln: String? =null, //상품 설명
    @ColumnInfo(name = "CALORY") val calorie: String? =null //열량
)