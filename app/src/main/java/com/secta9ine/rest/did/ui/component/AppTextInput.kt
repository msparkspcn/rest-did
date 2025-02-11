package com.secta9ine.rest.did.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.secta9ine.rest.did.ui.theme.RESTDIDTheme

@Composable
fun AppTextInput(
    text: String,
    modifier: Modifier = Modifier,
    focussed: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.body1,
    color: Color = Color.Black,
    paddingValues: PaddingValues = PaddingValues(horizontal = 6.dp, vertical = 15.dp),
) {

    val focusRequester = remember { FocusRequester() }
    Box(
        modifier = modifier
            .focusRequester(focusRequester)
            .clickable {
                focusRequester.requestFocus()
            }
    ) {
        BasicTextField(
            value = TextFieldValue(text, TextRange(text.length)),
            onValueChange = {},
            modifier = modifier
                .border(
                    width = 1.dp,
                    color = if (focussed) MaterialTheme.colors.primary else Color.Transparent
                )
                .padding(paddingValues),
            textStyle = textStyle.copy(color),
            singleLine = true,
            enabled = true
        )
    }

}

@Preview(showBackground = true)
@Composable
fun AppTextInputPreview() {
    RESTDIDTheme {
        AppTextInput(text = "매장코드")
    }
}