package com.secta9ine.rest.did.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "MST_DEVICE",
    primaryKeys = ["CMP_CD", "SALES_ORG_CD","STOR_CD", "CORNER_CD", "ITEM_CD"]
)
data class Device (
    @ColumnInfo(name = "CMP_CD") val cmpCd: String = "",
    @ColumnInfo(name = "SALES_ORG_CD") val salesOrgCd: String = "",
    @ColumnInfo(name = "STOR_CD") val storCd: String = "",
    @ColumnInfo(name = "CORNER_CD") val cornerCd: String = "",
    @ColumnInfo(name = "CORNER_NM") val cornerNm: String = "",
    @ColumnInfo(name = "USE_YN") val useYn: String = "",
)