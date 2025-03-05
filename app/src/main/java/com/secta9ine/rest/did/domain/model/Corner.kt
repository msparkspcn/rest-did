package com.secta9ine.rest.did.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "MST_CORNER",
    primaryKeys = ["CMP_CD", "SALES_ORG_CD","STOR_CD", "CORNER_CD"]
)
data class Corner (
    @ColumnInfo(name = "CMP_CD") val cmpCd: String = "",
    @ColumnInfo(name = "SALES_ORG_CD") val salesOrgCd: String = "",
    @ColumnInfo(name = "STOR_CD") val storCd: String = "",
    @ColumnInfo(name = "CORNER_CD") val cornerCd: String = "",
    @ColumnInfo(name = "CORNER_NM") val cornerNm: String = "",
    @ColumnInfo(name = "USE_YN") val useYn: String = "",
)