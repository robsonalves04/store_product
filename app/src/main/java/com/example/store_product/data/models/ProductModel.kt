package com.example.store_product.data.models

import androidx.compose.ui.graphics.vector.ImageVector

data class ProductModel(
    val id: Int,
    val title: String,
    val price: Double,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Int,
    val thumbnail: String,
    val description: String ,
    val category: String ,
    val brand: String ,
    val images: List<String>
)

data class ProductsResponseModel(
    val products: List<ProductModel>,
    val total: Int,
    val skip: Int,
    val limit: Int
)

data class OnBoardPage(
    val title: String,
    val description: String,
    val icon: ImageVector
)