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
import com.secta9ine.rest.did.presentation.navigation.NavUtils.navigateAsSecondScreen
import com.secta9ine.rest.did.presentation.navigation.Screen
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
        focusRequester.requestFocus()
        viewModel.uiState.collect {
            when(it) {
                is ProductViewModel.UiState.NavigateToDevice -> {
                    navController?.navigateAsSecondScreen(Screen.DeviceScreen.route)
                }
                is ProductViewModel.UiState.NavigateToOrderStatus -> {
                    navController?.navigateAsSecondScreen(Screen.OrderStatusScreen.route)
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
//            SingleProduct(productList = viewModel.productList)
            TwoProducts(productList = viewModel.productList)
//        ProductList(productList = viewModel.productList)
//        SpecialProductList(productList = viewModel.productList)
    }

}

