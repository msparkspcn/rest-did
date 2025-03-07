package com.secta9ine.rest.did.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.secta9ine.rest.did.network.WebSocketViewModel
import com.secta9ine.rest.did.presentation.device.DeviceScreen
import com.secta9ine.rest.did.presentation.login.LoginScreen
import com.secta9ine.rest.did.presentation.order.OrderStatusScreen
import com.secta9ine.rest.did.presentation.product.ProductScreen

private const val TAG = "AppNavHost"

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val webSocketViewModel: WebSocketViewModel = viewModel()
    DisposableEffect(navController) {
        val callback = NavController.OnDestinationChangedListener { _, destination, _ ->
            Log.d(TAG, "### 화면이동: route=${destination.route}")
        }
        navController.addOnDestinationChangedListener(callback)
        onDispose { navController.removeOnDestinationChangedListener(callback) }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.LoginScreen.route,
        modifier = modifier
    ) {
        composable(route = Screen.LoginScreen.route) {
            LoginScreen(navController = navController)
        }
        composable(route = Screen.DeviceScreen.route) {
            DeviceScreen(navController = navController)
        }
        composable(route = Screen.OrderStatusScreen.route) {
            OrderStatusScreen(
                navController = navController,
                viewModel2 = webSocketViewModel)
        }
        composable(route = Screen.ProductScreen.route) {
            ProductScreen(navController = navController)
        }
    }
}
