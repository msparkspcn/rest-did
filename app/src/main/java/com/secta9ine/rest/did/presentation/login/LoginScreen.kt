package com.secta9ine.rest.did.presentation.login

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.secta9ine.rest.did.R
import com.secta9ine.rest.did.presentation.navigation.Screen
import com.secta9ine.rest.did.ui.component.AppButton
import com.secta9ine.rest.did.ui.component.AppTextInput
import com.secta9ine.rest.did.ui.component.CheckedBox
import com.secta9ine.rest.did.ui.component.UncheckedBox
import com.secta9ine.rest.did.util.UiString

private const val TAG = "LoginScreen"
@Composable
fun LoginScreen(
    navController: NavHostController? = null,
    isFirstLaunch: Boolean,
    viewModel: LoginViewModel = hiltViewModel()
) {
    var dialogMessage by remember { mutableStateOf<UiString?>(null) }
    var dialogContents by remember { mutableStateOf<UiString?>(null) }
    val uiState by viewModel.uiState.collectAsState(initial = LoginViewModel.UiState.Idle)

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        Log.d(TAG,"최초진입 확인:$isFirstLaunch")
        if (isFirstLaunch) {
            viewModel.checkAutoLogin()
        }
    }
    LaunchedEffect(uiState) {
        Log.d(TAG, "111 uiState:$uiState")

        when (uiState) {
            is LoginViewModel.UiState.Login -> {
                navController?.navigate(Screen.DeviceScreen.route)
            }

            is LoginViewModel.UiState.Error -> {
                dialogMessage = UiString.TextString((uiState as LoginViewModel.UiState.Error).message)
            }

            else -> {}
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
            style = TextStyle(fontSize = 25.sp),
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(40.dp))
        AppTextInput(
            text = viewModel.userId,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .clickable { viewModel.onChangeFocus("userId") },
            color = if (viewModel.currentFocus == "userId") Color(0xFF1BAAFE) else Color(0xFFAFB7BF),
            focussed = viewModel.currentFocus == "userId",
            onChangeText = {viewModel.onChangeText("userId", it)},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            placeholder = stringResource(id = R.string.user_id),
            focusRequester = focusRequester
        )
        Spacer(Modifier.height(8.dp))
        AppTextInput(
            text = viewModel.password,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .clickable {
                    viewModel.onChangeFocus("password")
                    focusRequester.requestFocus()
                }
                .background(Color.White),
            color = if (viewModel.currentFocus == "password") Color(0xFF1BAAFE) else Color(0xFFAFB7BF),
            focussed = viewModel.currentFocus == "password",
            onChangeText = {viewModel.onChangeText("password", it)},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            placeholder = stringResource(id = R.string.user_password),
            focusRequester = focusRequester
        )
        Spacer(Modifier.height(16.dp))
        Box(modifier = Modifier.fillMaxWidth(0.5f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically // Align items vertically
            ) {

                AutoLoginCheckBox(
                    viewModel.isAutoLoginChecked,
                    changeAutoLoginChecked = {
                        viewModel.onChangeAutoLoginChecked(currentState = viewModel.isAutoLoginChecked)
                    })
                Spacer(Modifier.width(5.dp))
                Text(
                    text = stringResource(id = R.string.auto_login),
                    style = TextStyle(fontSize = 18.sp),
                )
            }
        }

        Spacer(Modifier.height(16.dp))
        AppButton(
            modifier = Modifier.fillMaxWidth(0.5f),
            onClick = viewModel::onLogin,
            colors = if(viewModel.userId!="" && viewModel.password!="") {
                ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF1BAAFE),
                    contentColor = Color.White
                )
            } else {
                ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFCFD4D8),
                    contentColor = Color.White
                )
            }

        )
        {
            Text(
                text = stringResource(R.string.login_button),
                style = TextStyle(fontSize = 35.sp)
            )
        }
    }
}

@Composable
fun AutoLoginCheckBox(
    isAutoLoginChecked: String,
    changeAutoLoginChecked: () -> Unit
) {
    Box(
        modifier = Modifier
            .clickable(onClick = changeAutoLoginChecked) // 클릭 이벤트 처리
    ) {
        if (isAutoLoginChecked == "Y") {
            CheckedBox()
        } else {
            UncheckedBox()
        }

    }
}