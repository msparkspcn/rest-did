package com.secta9ine.rest.did.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "MST_SALESORG",
    primaryKeys = ["CMP_CD", "SALES_ORG_CD"]
)
data class SalesOrg (
    @ColumnInfo(name = "CMP_CD") val cmpCd: String = "",
    @ColumnInfo(name = "SALES_ORG_CD") val salesOrgCd: String = "",
    @ColumnInfo(name = "SALES_ORG_NM") val salesOrgNm: String = "",
    @ColumnInfo(name = "USE_YN") val useYn: String = "",
)