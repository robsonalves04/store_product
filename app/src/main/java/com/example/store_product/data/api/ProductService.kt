package com.example.store_product.data.api

import com.example.store_product.data.models.ProductsResponseModel
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductService : IProductService {

    private val apiService: IProductService

    init {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        apiService = retrofit.create(IProductService::class.java)
    }

    //function to get a products from the API
    override suspend fun getProductList(): Response<ProductsResponseModel> {
        return apiService.getProductList()
    }
}