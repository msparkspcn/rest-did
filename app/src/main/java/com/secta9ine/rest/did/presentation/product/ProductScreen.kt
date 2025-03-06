package com.secta9ine.rest.did.presentation.product

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.secta9ine.rest.did.R
import com.secta9ine.rest.did.presentation.navigation.Screen
import com.secta9ine.rest.did.presentation.order.OrderStatusViewModel
import com.secta9ine.rest.did.util.UiString

@Composable
fun ProductScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController? = null,
    viewModel: ProductViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    var dialogMessage by remember { mutableStateOf<UiString?>(null)
    }
    val uiState by viewModel.uiState.collectAsState(initial = null)

    LaunchedEffect(Unit) {
        focusRequester.requestFocus() // Box가 포커스를 받도록 요청
        viewModel.uiState.collect {
            when(it) {
                is ProductViewModel.UiState.Device -> {
                    navController?.navigate(Screen.DeviceScreen.route)
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
            .focusable() // 키 입력을 받으려면 필수
            .onKeyEvent { keyEvent ->
                if (keyEvent.type == KeyEventType.KeyUp) {
                    viewModel.onEnterKeyPressed() // ViewModel에 이벤트 전달
                    Toast
                        .makeText(context, "Enter key pressed!", Toast.LENGTH_SHORT)
                        .show()

                    true
                } else {
                    false
                }
            }
    ) {
//            SingleProduct(productList = viewModel.productList)
//            TwoProducts(productList = viewModel.productList)
        ProductList(productList = viewModel.productList)
//        SpecialProductList(productList = viewModel.productList)
    }

}

