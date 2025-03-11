package com.secta9ine.rest.did.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppTextInput(
    text: String,
    modifier: Modifier = Modifier,
    focussed: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.body1,
    color: Color = Color.Black,
    paddingValues: PaddingValues = PaddingValues(horizontal = 15.dp, vertical = 15.dp),
    onChangeText: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    fontSize: TextUnit = 20.sp,
    shape: Shape = RoundedCornerShape(10.dp),
    placeholder: String,
    focusRequester: FocusRequester
) {
//    Log.d("AppTextInput","text:$text, focussed:$focussed, focusRequester:$focusRequester")
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current  // 추가

    Box(
        modifier = modifier
            .focusRequester(focusRequester)
            .onFocusChanged { isFocused = it.isFocused }
            .border(
                width = 1.dp, // 테두리 두께 조정
                color = if (isFocused) Color(0xFF1BAAFE) else Color(0xFFDEE0E6),
                shape = shape
            )

    ) {
        BasicTextField(
            value = text,
            onValueChange = {value -> onChangeText(value)},
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .background(Color.Transparent),
            textStyle = textStyle.copy(Color.Black, fontSize = fontSize),
            visualTransformation = if (keyboardOptions.keyboardType == KeyboardType.Password) PasswordVisualTransformation() else VisualTransformation.None,
            singleLine = true,
            enabled = true,
            keyboardOptions = KeyboardOptions(
                imeAction = keyboardOptions.imeAction,
            ),
            keyboardActions = KeyboardActions(  // 추가
                onDone = {
                    focusManager.clearFocus()
                },
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            decorationBox = { innerTextField ->
                Box {
                    if (text.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = Color.Gray,
                            fontSize = fontSize
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}