package com.example.store_product.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store_product.data.api.IProductService
import com.example.store_product.data.models.ProductModel
import com.example.store_product.local_data.ProductCache
import com.example.store_product.ui.components.network.networkConection
import com.example.store_product.ui.components.toats.toastSnackbar
import com.example.store_product.ui.components.utils.OnBoardingPreferences
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable.isActive
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException


class ProductViewModel @SuppressLint("StaticFieldLeak") constructor(private val productService: IProductService, private val context: Context) : ViewModel() {

    //job used to manage automatic reconnection attempts
    private var reconnectJob: Job? = null

    //flag to indicate if a reconnection attempt is in progress
    private var isTryingToReconnect = false

    //internal mutable list of product items loaded from API
    private val _getProduct = mutableStateListOf<ProductModel>()
    val getProduct: List<ProductModel> = _getProduct


    //flag representing the loading state (true when data is being fetched)
    private val _isLoaded = MutableStateFlow(false)
    val isLoaded: StateFlow<Boolean> = _isLoaded

    //marks onboarding as completed
    fun setOnBoardingCompleted() {
        viewModelScope.launch {
            OnBoardingPreferences.setCompleted(context, true)
        }
    }
    //fetches product from the API
    fun fetchProducts(context: Context) {
        viewModelScope.launch {
            try {
                val response = productService.getProductList()
                if (response.isSuccessful) {
                    val products = response.body()?.products.orEmpty()
                    _getProduct.clear()
                    _getProduct.addAll(products)
                    _isLoaded.value = true
                    //saves the list of products and records the last update time in cache
                    ProductCache.saveProducts(context, products)
                    ProductCache.saveLastUpdate(context)
                    Log.d("ProductVM", "Products fetched successfully from API: ${products.size}")

                } else {
                    toastSnackbar(context, "Failed to load products")
                }
            } catch (e: IOException) {
                tryReloadPeriodically(context)
                toastSnackbar(context, "Connection error. Trying again...")
            } catch (e: Exception) {
                toastSnackbar(context, "Unexpected error occurred")
            }
        }
    }
    //loads products from cache or fetches from API if cache is empty or expired
    fun loadOrFetchProducts(context: Context) {
        viewModelScope.launch {
            val cached = ProductCache.loadProducts(context)
            _getProduct.clear()
            _getProduct.addAll(cached)
            _isLoaded.value = true
            Log.d("ProductVM", "Products loaded locally from cache: ${cached.size}")

            if (ProductCache.isCacheExpired(context)) {

                Log.d("ProductVM", "Cache expired, loading products from API...")
                if (networkConection(context)) {
                    fetchProducts(context)
                    return@launch
                }else{
                    toastSnackbar(context, "No internet connection. Please check your network.",
                        backgroundColor = android.graphics.Color.parseColor("#FF0000"))
                    tryReloadPeriodically(context)
                }
            } else {
                Log.d("ProductVM", "Cache still valid, skipping API request.")
            }
        }
    }
    // loads products either from cache or fetches from API if needed
    fun loadProducts(context: Context) {
        viewModelScope.launch {
            val cached = ProductCache.loadProducts(context)
            _getProduct.clear()
            _getProduct.addAll(cached)
            _isLoaded.value = true
            if (!networkConection(context)) {
                toastSnackbar(context, "No internet connection. Please check your network.",
                    backgroundColor = android.graphics.Color.parseColor("#FF0000"))
                return@launch
            }
            Log.d("ProductVM", "Products loaded locally from cache: ${cached.size}")
        }
    }
    //retry request when connection is restored
    fun tryReloadPeriodically(context: Context) {
        if (isTryingToReconnect) return
        isTryingToReconnect = true
        reconnectJob = viewModelScope.launch {
            while (isActive) {
                delay(5000)

                if (networkConection(context)) {
                    toastSnackbar(context, "Connection restored! Reloading products...",
                        backgroundColor = android.graphics.Color.parseColor("#00FF00"),
                        textColor = android.graphics.Color.parseColor("#000000"))
                    fetchProducts(context)
                    isTryingToReconnect = false
                    break
                }
            }
        }
    }
}