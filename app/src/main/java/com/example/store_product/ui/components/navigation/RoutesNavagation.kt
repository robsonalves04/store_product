package com.example.store_product.ui.components.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object OnBoard : Screen("onboard")
    object ProductList : Screen("product_list")
    object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: Int) = "product_detail/$productId"
    }
}