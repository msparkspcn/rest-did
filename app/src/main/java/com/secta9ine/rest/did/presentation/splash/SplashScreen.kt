package com.secta9ine.rest.did.presentation.splash

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.secta9ine.rest.did.R
import com.secta9ine.rest.did.network.WebSocketViewModel
import com.secta9ine.rest.did.presentation.navigation.Screen
import com.secta9ine.rest.did.ui.component.AppAlertDialog
import com.secta9ine.rest.did.ui.component.AppButton
import com.secta9ine.rest.did.ui.component.AppLoadingIndicator
import com.secta9ine.rest.did.util.UiString

private const val TAG = "SplashScreen"
@Composable
fun SplashScreen(
    navController: NavHostController? = null,
    viewModel: SplashViewModel = hiltViewModel(),
    wsViewModel: WebSocketViewModel = hiltViewModel()
) {
    var dialogMessage by remember { mutableStateOf<UiString?>(null) }
    val uiState by viewModel.uiState.collectAsState(initial = SplashViewModel.UiState.Idle)
    val uiState2 by wsViewModel.uiState.collectAsState(initial = WebSocketViewModel.UiState.Idle)
    val permissionState = remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissionState.value = permissions.all { it.value }
        viewModel.onPermissionResult(permissionState.value)
    }
    val androidId by wsViewModel.androidId.collectAsState()
    LaunchedEffect(Unit) {
        launcher.launch(
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        )
    }

    LaunchedEffect(uiState) {
        Log.d(TAG, "111 uiState:$uiState")
        when (uiState) {
            is SplashViewModel.UiState.Login -> {
                navController?.navigate(Screen.DeviceScreen.route)
            }

            is SplashViewModel.UiState.UpdateDevice -> {
                Log.d(TAG,"### splash updateDevice")
                val displayCd = viewModel.getDisplayMenuCd()
                Log.d(TAG,"### displayCd:$displayCd")
                if(displayCd=="02") {
                    navController?.navigate(Screen.OrderStatusScreen.route)
                }
                else {
                    navController?.navigate(Screen.ProductScreen.route)
                }
            }
            is SplashViewModel.UiState.Error -> {
                dialogMessage = UiString.TextString((uiState as SplashViewModel.UiState.Error).message)
            }
            else -> {

            }
        }
    }
    LaunchedEffect(uiState2) {
        Log.d(TAG, "222 uiState:$uiState2")

        when (uiState2) {
            is WebSocketViewModel.UiState.UpdateDevice -> {
                Log.d(TAG,"ws1 updateDevice")
                //use case 에서 장비 설정 완료 후 display 할 화면으로 이동
//                viewModel.getDevice()
                viewModel.updateUiState(SplashViewModel.UiState.UpdateDevice)
            }
            is WebSocketViewModel.UiState.CheckDevice -> {
                viewModel.checkDevice()
            }
//            is WebSocketViewModel.UiState.Error -> {
//                dialogMessage = UiString.TextString((uiState as SplashViewModel.UiState.Error).message)
//            }
            else -> {

            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = androidId,
                style = TextStyle(fontSize = 40.sp),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(id = R.string.req_register_device),
                style = TextStyle(fontSize = 25.sp),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(40.dp))
        }
        if (dialogMessage != null) {
            AppAlertDialog {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .fillMaxHeight(0.6f)
                        .background(Color.White),
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(id = R.string.alert_title),
                                style = TextStyle(fontSize = 30.sp)
                            )
                            Spacer(modifier = Modifier.height(30.dp))
                            Text(
                                text = dialogMessage?.asString() ?: "",
                                style = TextStyle(fontSize = 25.sp)
                            )
                        }

                        // 버튼을 하단에 배치
                        AppButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { dialogMessage = null },
                            shape = RoundedCornerShape(0.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.confirm),
                                style = TextStyle(fontSize = 28.sp)
                            )
                        }
                    }
                }
            }
        }
        if (uiState is SplashViewModel.UiState.Loading) {
            AppLoadingIndicator()
        }
    }
}