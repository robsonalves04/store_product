package com.example.store_product.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.imageLoader
import coil.request.ImageRequest
import com.example.store_product.ui.components.utils.ProductItemCard
import com.example.store_product.ui.components.navigation.Screen
import com.example.store_product.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenProductList(
    navController: NavController,
    viewModel: ProductViewModel
) {
    //current context from the composition
    val context = LocalContext.current
    //retrieves the list of products from the view model
    val productList = viewModel.getProduct
    //observes whether the product data has been successfully loaded
    val isLoaded by viewModel.isLoaded.collectAsState()
    //loader instance from the current context to handle image requests and caching
    val imageLoader = LocalContext.current.imageLoader
    // preload images when the product list changes
    LaunchedEffect(productList) {
        productList.forEach { product ->
            val request = ImageRequest.Builder(context)
                .data(product.thumbnail)
                .build()
            imageLoader.enqueue(request)
        }
    }

    //loads initial data when the screen is first displayed
    LaunchedEffect(Unit) {
        viewModel.loadOrFetchProducts(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "\uD83D\uDED2 Products",
                        style = androidx.compose.material.MaterialTheme.typography.h5
                    )
                },
            )
        }
    ) { paddingValues ->
        if (isLoaded && productList.isEmpty()) {
            //box loading progress
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(Color(0xFFF8F8F8)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "⏳ Loading Products...",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(Color(0xFFF8F8F8)),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(productList.chunked(2)) { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        for (product in rowItems) {
                            //displays a single product item card with fixed aspect ratio and weight
                            ProductItemCard(
                                product = product,
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f),
                                onClick = {
                                    navController.navigate(Screen.ProductDetail.createRoute(product.id))
                                }
                            )
                        }
                        //fills space if the number of items is odd
                        if (rowItems.size < 2) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}