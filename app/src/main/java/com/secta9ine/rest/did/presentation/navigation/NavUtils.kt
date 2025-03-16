package com.secta9ine.rest.did.presentation.navigation

import androidx.navigation.NavHostController

object NavUtils {
    fun NavHostController.navigateAsSecondScreen(route: String) {
        navigate(route) {
            popUpTo(Screen.SplashScreen.route) {
                inclusive = false
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}