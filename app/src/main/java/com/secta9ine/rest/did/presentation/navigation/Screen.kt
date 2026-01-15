package com.secta9ine.rest.did.presentation.navigation

sealed class Screen(val route: String) {
    object LoginScreen : Screen("LoginScreen")
    object DeviceScreen : Screen("DeviceScreen")
    object OrderStatusScreen : Screen("OrderStatusScreen")
    object ProductScreen : Screen("ProductScreen")
    object SplashScreen : Screen("SplashScreen")
    object AdvertScreen : Screen("AdvertScreen")
}