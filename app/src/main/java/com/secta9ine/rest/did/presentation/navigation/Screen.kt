package com.secta9ine.rest.did.presentation.navigation

sealed class Screen(val route: String) {
    object LoginScreen : Screen("LoginScreen")
    object DeviceScreen : Screen("DeviceScreen")
}