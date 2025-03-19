package com.secta9ine.rest.did.presentation.product

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.secta9ine.rest.did.network.WebSocketViewModel
import com.secta9ine.rest.did.presentation.navigation.NavUtils.navigateAsSecondScreen
import com.secta9ine.rest.did.presentation.navigation.Screen
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

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        viewModel.uiState.collect {
            when(it) {
                is ProductViewModel.UiState.UpdateDevice -> {
                    var displayCd = viewModel.getDisplayCd()
                    if(displayCd=="1234") {
                        navController?.navigateAsSecondScreen(Screen.OrderStatusScreen.route)
                    }
                    else {
                        //같은 화면으로 이동할 필요 없고 업데이트된 로컬 db에서 조회에서 데이터 렌더링 다시 실행
                    }
                }

                is ProductViewModel.UiState.Error -> {
                    dialogMessage = UiString.TextString(it.message)
                }
                else -> {}
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
                    viewModel.onEnterKeyPressed()
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
                    productList = viewModel.productList,
                    rollingYn = viewModel.rollingYn)
            }
            "03" -> {
                TwoProducts(
                    productList = viewModel.productList,
                    rollingYn = viewModel.rollingYn)
            }
            "04" -> {
                ProductList(
                    productList = viewModel.productList,
                    rollingYn = viewModel.rollingYn)
            }
            "05" -> {
                SpecialProductList(
                    productList = viewModel.productList,
                    rollingYn = viewModel.rollingYn)
            }
        }
    }

}

