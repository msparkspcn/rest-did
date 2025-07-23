package com.secta9ine.rest.did.data.remote.dto

data class OrderStatusRequestDto(
    val cmpCd: String,
    val salesOrgCd: String,
    val storCd: String,
    val cornerCd: String,
    val searchDt: String,
)