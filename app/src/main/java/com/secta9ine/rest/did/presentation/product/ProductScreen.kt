package com.secta9ine.rest.did.presentation.product

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.secta9ine.rest.did.network.WebSocketViewModel
import com.secta9ine.rest.did.presentation.navigation.NavUtils.navigateAsSecondScreen
import com.secta9ine.rest.did.presentation.navigation.Screen
import com.secta9ine.rest.did.presentation.splash.SplashViewModel
import com.secta9ine.rest.did.util.CommonUtils
import com.secta9ine.rest.did.util.UiString

private const val TAG = "ProductScreen"
@Composable
fun ProductScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController? = null,
    viewModel: ProductViewModel = hiltViewModel(),
    wsViewModel: WebSocketViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    var dialogMessage by remember { mutableStateOf<UiString?>(null)
    }
    val uiState by viewModel.uiState.collectAsState(initial = null)
    val uiState2 by wsViewModel.uiState.collectAsState(initial = WebSocketViewModel.UiState.Idle)
    val productList by viewModel.productList.collectAsState()
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        requestPermissions(context)

        viewModel.uiState.collect {
            when(it) {
                is ProductViewModel.UiState.UpdateDevice -> {
                    var displayCd = viewModel.getDisplayCd()

//                    if(displayCd=="1234") {
//                        navController?.navigateAsSecondScreen(Screen.OrderStatusScreen.route)
//                    }
//                    else {
                        //같은 화면으로 이동할 필요 없고 업데이트된 로컬 db에서 조회에서 데이터 렌더링 다시 실행
//                    }
                }

                is ProductViewModel.UiState.Error -> {
                    dialogMessage = UiString.TextString(it.message)
                }
                else -> {}
            }
        }
    }
    LaunchedEffect(uiState2) {
        Log.d(TAG, "222 uiState:$uiState2")

        when (uiState2) {
            is WebSocketViewModel.UiState.UpdateDevice -> {
                Log.d(TAG,"ws updateDevice")
                //usecase 에서 장비 설정 완료 후 display할 화면으로 이동
//                viewModel.getDevice()
//                viewModel.updateVersion(context)
            }
            is WebSocketViewModel.UiState.SoldOut -> {
                val data = (uiState2 as WebSocketViewModel.UiState.SoldOut).data
                Log.d(TAG, "상품 품절 처리: $data")
                viewModel.updateSoldoutYn(data)
            }
            else -> {

            }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .focusRequester(focusRequester) // 포커스 요청
            .focusable()
            .onKeyEvent { keyEvent ->
                if (keyEvent.type == KeyEventType.KeyUp) {
                    viewModel.onEnterKeyPressed(context)
                    Toast
                        .makeText(context, "Enter key pressed!", Toast.LENGTH_SHORT)
                        .show()

                    true
                } else {
                    false
                }
            }
    ) {
        when (viewModel.displayCd) {
            "01" -> {
                SingleProduct(
                    productList = productList,
                    rollingYn = viewModel.rollingYn)
            }
            "03" -> {
                TwoProducts(
                    productList = productList,
                    rollingYn = viewModel.rollingYn)
            }
            "04" -> {
                ProductList(
                    productList = productList,
                    rollingYn = viewModel.rollingYn,
                    version= CommonUtils.getAppVersion(context)

                )

            }
            "05" -> {
                SpecialProductList(
                    productList = productList,
                    rollingYn = viewModel.rollingYn)
            }
        }
    }

}
private fun requestPermissions(context: Context) {
    val isPermit =canRequestInstallPackages(context)
    Log.d(TAG,"업데이트 테스트 권한:"+canRequestInstallPackages(context))
    Log.d(TAG,"권한 요청")
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

        // 권한 요청
        ActivityCompat.requestPermissions(context as Activity,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
            1000)
    }
    else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !canRequestInstallPackages(context)) {
        // INSTALL_PACKAGES 권한 요청
        val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
        intent.data = Uri.parse("package:${context.packageName}")
        context.startActivity(intent)
    }
    else {
        Log.d(TAG,"권한 있음")
    }
}

private fun canRequestInstallPackages(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.packageManager.canRequestPackageInstalls()
    } else {
        true
    }
}
