package com.secta9ine.rest.did.util

import androidx.room.TypeConverter

object Converters {

    // Set<String>을 구분자로 연결된 단일 String으로 변환
    @TypeConverter
    @JvmStatic
    fun fromStringSetToString(set: Set<String>?): String? {
        // Set이 null이면 null 반환, 아니면 '|'로 구분하여 문자열로 연결
        // '|' 대신 콤마(,), 세미콜론(;) 등 Set 요소에 포함될 가능성이 적은 다른 구분자를 사용해도 됩니다.
        return set?.joinToString(separator = "|")
    }

    // String을 Set<String>으로 변환
    @TypeConverter
    @JvmStatic
    fun toStringSetFromString(data: String?): Set<String>? {
        // 문자열이 null이거나 비어있으면 빈 Set 또는 null 반환
        return if (data.isNullOrEmpty()) {
            emptySet() // 비어있는 Set을 반환하는 것이 더 안전할 수 있습니다.
        } else {
            data.split("|").toSet() // '|' 구분자로 분리하여 Set으로 변환
        }
    }
}