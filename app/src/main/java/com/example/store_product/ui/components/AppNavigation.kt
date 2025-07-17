package com.example.store_product.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.store_product.ui.screen.ScreenOnboard
import com.example.store_product.ui.screen.ScreenSplash

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = "splash") {
        //initial splash screen displayed when the app launches
        composable("splash") {
            ScreenSplash(
                navigateToNextScreen = {
                    navController.navigate("onboard") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }
        //screen shown during the onboarding process
        composable("onboard") {
            ScreenOnboard()
        }
    }
}