package com.secta9ine.rest.did.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "STORE",
    primaryKeys = ["STORE_CD"]
)
data class Store (
    @ColumnInfo(name = "SEQ") val seq: Long? = null, // SEQ 번호
    @ColumnInfo(name = "STORE_CD") val storeCd: String = "",
    @ColumnInfo(name = "STORE_NM") val storeNm: String = "",
)