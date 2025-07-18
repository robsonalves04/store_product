package com.example.store_product.ui.components.navigation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.store_product.ui.components.utils.OnBoardingPreferences
import com.example.store_product.ui.screen.ScreenOnboard
import com.example.store_product.ui.screen.ScreenProductDetail
import com.example.store_product.ui.screen.ScreenProductList
import com.example.store_product.ui.screen.ScreenSplash
import com.example.store_product.viewmodel.ProductViewModel
import org.koin.androidx.compose.koinViewModel

@SuppressLint("RememberReturnType")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {
    //gets an instance of the ProductViewModel using Koin
    val dataViewModel: ProductViewModel = koinViewModel()
    //creates and remembers the navigation controller
    val navController = rememberNavController()
    //accesses the current context
    val context = LocalContext.current
    //observes whether onboarding has been completed using a Flow
    val completed by OnBoardingPreferences.completedFlow(context).collectAsState(initial = false)
    //sets the start destination based on whether onboarding was completed
    val startDestination = if (completed) Screen.Splash.route else Screen.OnBoard.route

    NavHost(navController = navController, startDestination = startDestination) {
        //screen shown during the onboarding process
        composable(Screen.Splash.route) {
            ScreenSplash(
                navigateToProductList = {
                    navController.navigate(Screen.ProductList.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        //initial splash screen displayed when the app launches
        composable(Screen.OnBoard.route) {
            ScreenOnboard(
                onFinishOnBoarding = {
                    navController.navigate(Screen.Splash.route) {
                        popUpTo(Screen.OnBoard.route) { inclusive = true }
                    }
                    dataViewModel.setOnBoardingCompleted()
                })
        }
        //displays the product list
        composable(Screen.ProductList.route) {
            ScreenProductList( navController,
                viewModel = dataViewModel)
        }
        //displays the product details
        composable(Screen.ProductDetail.route) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
            productId?.let {
                ScreenProductDetail(productId = it, viewModel = dataViewModel,
                    onBack = {navController.navigate(Screen.ProductList.route)})
            }
        }
    }
}