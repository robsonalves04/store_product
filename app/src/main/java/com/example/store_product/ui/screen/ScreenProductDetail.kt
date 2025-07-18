package com.example.store_product.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.store_product.R
import com.example.store_product.ui.components.utils.ratingIcon
import com.example.store_product.viewmodel.ProductViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenProductDetail(
    productId: Int,
    viewModel: ProductViewModel,
    onBack: () -> Unit = {}
) {
    //current context from the composition
    val context = LocalContext.current
    //retrieves the list of products from the view model
    val productList = viewModel.getProduct

    //loading of products data storange
    LaunchedEffect(Unit) {
        viewModel.loadProducts(context)
    }

    val product = productList.firstOrNull { it.id == productId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(" \uD83D\uDCC4 Product Details", style = MaterialTheme.typography.h5) },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (product == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                AsyncImage(
                    model = product.thumbnail,
                    contentDescription = product.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp)),
                    placeholder = painterResource(id = R.drawable.baseline_remove_shopping_cart_24),
                    error = painterResource(id = R.drawable.ic_launcher_product_foreground),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    product.title,
                    style = MaterialTheme.typography.h5,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(12.dp))
                LabeledText(label = "Preço", value = "€${product.price}")
                LabeledText(label = "Desconto", value = "${product.discountPercentage}%")
                LabeledText(label = "Stock", value = "${product.stock}")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    LabeledText(label = "Rating", value = "${product.rating}")
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = ratingIcon(product.rating),
                        contentDescription = null,
                        tint = Color(0xFF673AB7),
                        modifier = Modifier.size(22.dp).alignByBaseline()
                    )
                }
                LabeledText(label = "Categoria", value = product.category)
                LabeledText(label = "Marca", value = product.brand)
            }
        }
    }
}

@Composable
fun LabeledText(label: String, value: String?) {
    Text(
        buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append("$label: ")
            }
            append(value ?: "N/A")
        },
        style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}