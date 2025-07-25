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

private const val TAG = "ProductList"
@Composable
fun ProductList(
    productList: List<ProductVo>,
    rollingYn: String,
    version:String
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    var displayedProducts by remember { mutableStateOf(productList.take(8)) }
    var productIndex by remember { mutableStateOf(0) }


    LaunchedEffect(productList, rollingYn) {
        Log.d(TAG,"productList, rollingYn 변경")
        if(productList.size<=8) {
            displayedProducts = productList.take(8)
        }
        else if(rollingYn=="Y"){
            productIndex = 0
            while(true) {
                val endIndex = minOf(productIndex + 8, productList.size)
                displayedProducts = productList.subList(productIndex, endIndex)

                delay(5000)

                productIndex = if (endIndex == productList.size) 0 else endIndex
            }
        }
    }
    val fullProductList = displayedProducts + List(8 - displayedProducts.size) { null }

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
                fullProductList.chunked(2).forEach { rowItems ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = screenWidth * 0.02f)
                            .weight(1f),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        rowItems.forEach { item ->
                            if (item != null) {
                                Item(
                                    item = item,
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .padding(horizontal = screenWidth * 0.02f)
                                )
                            } else {
                                Spacer(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .padding(horizontal = screenWidth * 0.02f)
                                )
                            }
                        }
                        if (rowItems.size == 1) {
                            Spacer(
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
//        Text(
//            text = " 메뉴 $version",
//            style = MaterialTheme.typography.h5,
//            color = Color.Yellow,
//        )
    }
}