package com.secta9ine.rest.did.data.remote.dto

data class DeviceRequestDto(
    val cmpCd: String,
    val salesOrgCd: String,
    val cornerCd: String,
    val storCd: String,
    val deviceId: String,
    val deviceNo: String
)