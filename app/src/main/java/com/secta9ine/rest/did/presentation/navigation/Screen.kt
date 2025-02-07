package com.secta9ine.rest.did.presentation.navigation

sealed class Screen(val route: String) {
    data object LoginScreen : Screen("LoginScreen")
    data object DeviceScreen : Screen("DeviceScreen")
}