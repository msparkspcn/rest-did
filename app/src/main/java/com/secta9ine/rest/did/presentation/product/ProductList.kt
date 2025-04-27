package com.secta9ine.rest.did.presentation.product

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.secta9ine.rest.did.domain.model.Product
import kotlinx.coroutines.delay

private const val TAG = "ProductList"
@Composable
fun ProductList(
    productList: List<Product>,
    rollingYn: String,
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    var displayedProducts by remember { mutableStateOf(productList.take(8)) }
    var productIndex by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        if(productList.size<9) {
            displayedProducts = productList
        } else {
            while(true) {
                delay(5000)
//                Log.d(TAG,"productIndex:$productIndex, displayedProducts:$displayedProducts")

                if(productIndex + 8 <= productList.size) {
                    displayedProducts = productList.subList(productIndex, productIndex+8)
                    productIndex += 8
                }
                else {
                    displayedProducts = productList.subList(productIndex, productList.size)
                    productIndex = 0
                }
            }
        }
    }
    Column {
        Header()
        Box(
            modifier = Modifier
                .fillMaxSize() // 전체 화면을 100% 차지
                .background(Color(0xFFE0E0E0)) // 배경색 설정
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.8f) // 가로 70% 차지
                    .fillMaxHeight(0.9f) // 세로 70% 차지
                    .align(Alignment.Center) // 가운데 정렬
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center, // 세로 가운데 정렬
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                displayedProducts.take(8).chunked(2).forEach { rowItems ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = screenWidth * 0.02f)
                            .weight(1f),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        rowItems.forEach { item ->
                            Item(
                                item = item,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .padding(horizontal = screenWidth * 0.02f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Header() {
    Row(
        modifier = Modifier
            .background(Color(0xFF283237))
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "SR DID",
            style = MaterialTheme.typography.h4,
            color = Color.White
        )
        Text(
            text = " 메뉴",
            style = MaterialTheme.typography.h5,
            color = Color.Yellow,
        )
    }
}