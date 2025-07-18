package com.example.store_product.data.api


import com.example.store_product.data.models.ProductsResponseModel
import retrofit2.Response
import retrofit2.http.GET


interface IProductService {
    // GET - product list
    @GET("products")
    suspend fun getProductList(): Response<ProductsResponseModel>
}