package com.secta9ine.rest.did.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "SYS_USER_INFO",
    primaryKeys = ["CMP_CD","USER_ID"]
)
data class User(
    @ColumnInfo(name = "CMP_CD") val cmpCd: String = "",
    @ColumnInfo(name = "USER_ID") val userId: String = "",
    @ColumnInfo(name = "USER_ROLE_TYPE") val userRoleType : String? = null,
    @ColumnInfo(name = "SALES_ORG_CD") val salesOrgCd : String? = null,
    @ColumnInfo(name = "STOR_CD") val storCd : String? = null,
    @ColumnInfo(name = "LANG_SETTING") val langSettng : String? = null,
    @ColumnInfo(name = "USER_NM") val userNm : String? = null,
    @ColumnInfo(name = "EMP_NO") val empNo : String? = null,
    @ColumnInfo(name = "USE_YN") val useYn : String? = null,
)