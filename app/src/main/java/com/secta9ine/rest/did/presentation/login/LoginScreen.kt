package com.secta9ine.rest.did.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.secta9ine.rest.did.R
import com.secta9ine.rest.did.presentation.navigation.Screen
import com.secta9ine.rest.did.ui.component.AppButton
import com.secta9ine.rest.did.ui.component.AppTextInput
import com.secta9ine.rest.did.ui.theme.RESTDIDTheme
import com.secta9ine.rest.did.util.UiString

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController? = null,
    viewModel: LoginViewModel = hiltViewModel()
) {
    var dialogMessage by remember { mutableStateOf<UiString?>(null) }
    var dialogContents by remember { mutableStateOf<UiString?>(null) }
    val uiState by viewModel.uiState.collectAsState(initial = null)

    LaunchedEffect(Unit) {
        viewModel.uiState.collect {
            when(it) {
                is LoginViewModel.UiState.Login -> {
                    navController?.navigate(Screen.DeviceScreen.route)
                }
                is LoginViewModel.UiState.Error -> {
                    dialogMessage = UiString.TextString(it.message)
                }
                else -> {}
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "SR DID",
            style = TextStyle(fontSize = 40.sp),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "MEMBER LOGIN",
            style = TextStyle(fontSize = 30.sp),
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(40.dp))
        AppTextInput(
            text = viewModel.userId,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .background(Color(0xFF37454B))
                .clickable { viewModel.onChangeFocus("userId") },
            color = Color(0xFFFFFFFF),
            focussed = viewModel.currentFocus == "userId",
            onChangeText = {viewModel.onChangeText("userId", it)},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = stringResource(id = R.string.user_id)
        )
        Spacer(Modifier.height(8.dp))
        AppTextInput(
            text = viewModel.password,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .background(Color(0xFF37454B))
                .clickable { viewModel.onChangeFocus("password") },
            color = Color(0xFFFFFFFF),
            focussed = viewModel.currentFocus == "password",
            onChangeText = {viewModel.onChangeText("password", it)},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            placeholder = stringResource(id = R.string.user_password)
        )
        Spacer(Modifier.height(16.dp))
        AppButton(
            modifier = Modifier.fillMaxWidth(0.5f),
            onClick = viewModel::onLogin) {
            Text(
                text = stringResource(R.string.login_button),
                style = TextStyle(fontSize = 35.sp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    RESTDIDTheme {
        LoginScreen()
    }
}