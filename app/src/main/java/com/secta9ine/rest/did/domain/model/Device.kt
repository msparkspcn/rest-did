package com.secta9ine.rest.did.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "DID_DEVICE_INFO",
    primaryKeys = ["DEVICE_ID"]
)
data class Device (
    @ColumnInfo(name = "DEVICE_ID") val deviceId: String = "",
    @ColumnInfo(name = "DEVICE_NO") val deviceNo: String? = null,
    @ColumnInfo(name = "CMP_CD") val cmpCd: String? = null,
    @ColumnInfo(name = "SALES_ORG_CD") val salesOrgCd: String? = null,
    @ColumnInfo(name = "STOR_CD") val storCd: String? = null,
    @ColumnInfo(name = "CORNER_CD") val cornerCd: String? = null,
    @ColumnInfo(name = "DISPLAY_MENU_CD") val displayMenuCd: String? = null,
    @ColumnInfo(name = "ROLLING_YN") val rollingYn: String? = null,
    @ColumnInfo(name = "API_KEY") val apiKey: String? = null,
    @ColumnInfo(name = "USE_YN") val useYn: String? = null,
)