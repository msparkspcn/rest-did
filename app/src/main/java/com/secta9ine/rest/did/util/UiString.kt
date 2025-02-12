package com.secta9ine.rest.did.util

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource

sealed interface UiString {
    data class TextString(val value: String) : UiString
    class ResString(@StringRes val id: Int, val args: Array<Any> = emptyArray()) : UiString
    class ResStrings(val seperater: String = "", @StringRes vararg val ids : Int) : UiString
    @Composable
    fun asString() = when (this) {
        is TextString -> value
        is ResString -> stringResource(id, args)
        is ResStrings -> {
            val context = LocalContext.current
            ids.joinToString (separator = seperater) { context.getString(it) }
        }
    }
}