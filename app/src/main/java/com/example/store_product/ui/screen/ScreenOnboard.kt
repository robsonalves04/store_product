package com.example.store_product.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.store_product.data.models.OnBoardPage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch


@OptIn(ExperimentalPagerApi::class)
@Composable
fun ScreenOnboard(
    onFinishOnBoarding: () -> Unit
) {
    //remembers a coroutine scope
    val scope = rememberCoroutineScope()
    // list of onboarding pages with title, description, and icon
    val pages = listOf(
        OnBoardPage("Bem-vindo!", "Descubra produtos incríveis com apenas um toque.", Icons.Default.ShoppingCart),
        OnBoardPage("Detalhes completos", "Veja avaliações, preços e promoções.", Icons.Default.Star),
        OnBoardPage("Comece agora", "Vamos explorar juntos!", Icons.Default.CheckCircle)
    )
    //remembers the state of the pager
    val pagerState = rememberPagerState()

    Scaffold(
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                HorizontalPagerIndicator(pagerState = pagerState)
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = onFinishOnBoarding) {
                        Text("Pular")
                    }
                    Button(
                        onClick = {
                            scope.launch {
                                if (pagerState.currentPage == pages.lastIndex) {
                                    onFinishOnBoarding()
                                } else {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            }
                        }
                    ) {
                        Text(if (pagerState.currentPage == pages.lastIndex) "Começar" else "Próximo")
                    }
                }
            }
        }
    ) { padding ->
        HorizontalPager(
            count = pages.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) { page ->
            val currentPage = pages[page]
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = currentPage.icon,
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = currentPage.title,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = currentPage.description,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}