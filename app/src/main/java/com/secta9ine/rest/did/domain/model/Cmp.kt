package com.secta9ine.rest.did.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "MST_CMP",
    primaryKeys = ["CMP_CD"]
)
data class Cmp (
    @ColumnInfo(name = "CMP_CD") val cmpCd: String = "",
    @ColumnInfo(name = "CMP_NM") val cmpNm: String = "",
    @ColumnInfo(name = "USE_YN") val useYn: String = "",
)