package com.secta9ine.rest.did.presentation.product

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.secta9ine.rest.did.domain.model.ProductVo
import kotlinx.coroutines.delay

private const val TAG = "ProductList2"
@Composable
fun ProductList2(
    productList: List<ProductVo>,
    rollingYn: String,
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    var displayedProducts by remember { mutableStateOf(productList.take(12)) }
    var productIndex by remember { mutableStateOf(0) }


    LaunchedEffect(productList, rollingYn) {
        Log.d(TAG,"productList, rollingYn 변경")
        if(productList.size<=12) {
            displayedProducts = productList.take(12)
        }
        else if(rollingYn=="Y"){
            productIndex = 0
            while(true) {
                val endIndex = minOf(productIndex + 12, productList.size)
                displayedProducts = productList.subList(productIndex, endIndex)

                delay(5000)

                productIndex = if (endIndex == productList.size) 0 else endIndex
            }
        }
    }
    val fullProductList = displayedProducts + List(12 - displayedProducts.size) { null }

    Column {
        Header2()
        Box(
            modifier = Modifier
                .fillMaxSize() // 전체 화면을 100% 차지
                .background(Color(0xFFE0E0E0)) // 배경색 설정
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                fullProductList.chunked(6).forEach { rowItems ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = screenWidth * 0.01f)
                            .weight(1f),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        rowItems.forEach { item ->
                            if (item != null) {
                                Item2(
                                    item = item,
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .padding(horizontal = screenWidth * 0.005f), // Reduced padding to maximize size
                                )
                            } else {
                                Spacer(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .padding(horizontal = screenWidth * 0.01f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Header2() {
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
    }
}