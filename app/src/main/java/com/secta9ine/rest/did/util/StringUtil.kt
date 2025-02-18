package com.secta9ine.rest.did.util

import java.text.DecimalFormat

fun String.formatCurrency(pattern: String = "#,###"): String? {
    return try {
        DecimalFormat(pattern).format(this.toBigDecimal())
    } catch (e: Exception) {
        null
    }
}