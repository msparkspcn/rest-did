package com.secta9ine.rest.did.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "MST_DEVICE",
    primaryKeys = ["CMP_CD", "SALES_ORG_CD","STOR_CD", "CORNER_CD", "DEVICE_ID"]
)
data class Device (
    @ColumnInfo(name = "DEVICE_ID") val deviceId: String = "",
    @ColumnInfo(name = "DEVICE_NO") val deviceNo: String = "",
    @ColumnInfo(name = "CMP_CD") val cmpCd: String = "",
    @ColumnInfo(name = "SALES_ORG_CD") val salesOrgCd: String = "",
    @ColumnInfo(name = "STOR_CD") val storCd: String = "",
    @ColumnInfo(name = "CORNER_CD") val cornerCd: String = "",
    @ColumnInfo(name = "DISPLAY_MENU_CD") val displayMenuCd: String = "",
    @ColumnInfo(name = "DISPLAY_MENU_NM") val displayMenuNm: String = "",
    @ColumnInfo(name = "API_KEY") val apiKey: String = "",
    @ColumnInfo(name = "USE_YN") val useYn: String = "",
)