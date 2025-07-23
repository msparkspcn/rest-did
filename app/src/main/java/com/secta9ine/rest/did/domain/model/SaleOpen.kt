package com.secta9ine.rest.did.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "SALE_OPEN",
    primaryKeys = ["CMP_CD", "SALES_ORG_CD", "STOR_CD", "SALE_DT"]
)
data class SaleOpen (
    @ColumnInfo(name = "CMP_CD") val cmpCd: String = "",
    @ColumnInfo(name = "SALES_ORG_CD") val salesOrgCd: String = "",
    @ColumnInfo(name = "STOR_CD") val storCd: String = "",
    @SerializedName("openDt")
    @ColumnInfo(name = "SALE_DT") val saleDt: String = ""
)