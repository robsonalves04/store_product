package com.example.store_product.ui.components.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun ratingIcon(rating: Double): ImageVector {
    return when {
        rating < 3.0 -> Icons.Default.ThumbDown
        rating < 4.0 -> Icons.Default.EmojiEmotions
        else -> Icons.Default.ThumbUp
    }
}