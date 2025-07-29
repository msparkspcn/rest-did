package com.secta9ine.rest.did.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore

@Entity(
    tableName = "DID_DEVICE_INFO",
    primaryKeys = ["DEVICE_ID"]
)
data class Device (
    @ColumnInfo(name = "DEVICE_ID") var deviceId: String = "",
    @ColumnInfo(name = "DEVICE_NO") var deviceNo: String? = null,
    @ColumnInfo(name = "CMP_CD") var cmpCd: String? = null,
    @ColumnInfo(name = "SALES_ORG_CD") var salesOrgCd: String? = null,
    @ColumnInfo(name = "STOR_CD") var storCd: String? = null,
    @ColumnInfo(name = "CORNER_CD") var cornerCd: String? = null,
    @ColumnInfo(name = "DISPLAY_MENU_CD") var displayMenuCd: String? = null,
    @ColumnInfo(name = "ROLLING_YN") var rollingYn: String? = null,
    @Ignore var apiKey: String? = null,
    @ColumnInfo(name = "USE_YN") var useYn: String? = null,
    @ColumnInfo(name = "MESSAGE1") var message1: String? = null,
    @ColumnInfo(name = "MESSAGE2") var message2: String? = null,
    @ColumnInfo(name = "MESSAGE3") var message3: String? = null,
    @ColumnInfo(name = "DISPLAY_CORNER") var displayCorner: Set<String>? = null
)