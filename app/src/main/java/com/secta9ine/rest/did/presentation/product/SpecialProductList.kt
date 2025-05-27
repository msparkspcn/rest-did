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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.secta9ine.rest.did.domain.model.Product
import kotlinx.coroutines.delay
private const val TAG = "SpecialProductList"
@Composable
fun SpecialProductList(
    productList: List<Product>,
    rollingYn: String,
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    Column {
        SpecialProductHeader()
        SpecialProductContents(
            productList = productList,
            rollingYn = rollingYn,
            screenWidth = screenWidth,
            screenHeight = screenHeight
        )
    }
}

@Composable
fun SpecialProductHeader() {
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
            text = "  Special Menu",
            style = MaterialTheme.typography.h5,
            color = Color.Yellow,
        )
    }
}

@Composable
fun SpecialProductContents(
    productList: List<Product>,
    rollingYn: String,
    screenWidth: Dp,
    screenHeight:Dp,
) {
    var displayedSpecialProducts by remember { mutableStateOf(productList.take(3)) }
    var productIndex by remember { mutableStateOf(0) }
    LaunchedEffect(productList) {
        Log.d(TAG,"productList 변경")
        // 새로운 productList가 들어올 때마다 초기화
        productIndex = 0
        displayedSpecialProducts = productList.take(3)
    }

    LaunchedEffect(productList, rollingYn) {
        Log.d(TAG,"productList, rollingYn 변경")
        if(productList.size>3 &&rollingYn=="Y") {
            while (true) {
                delay(5000)

                if(productIndex + 3 <= productList.size) {
                    displayedSpecialProducts = productList.subList(productIndex, productIndex+3)
                    productIndex += 3
                }
                else {
                    displayedSpecialProducts = productList.subList(productIndex, productList.size)
                    productIndex = 0
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize() // 전체 화면을 100% 차지
            .background(Color(0xFFE0E0E0)) // 배경색 설정
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .align(Alignment.Center)
                .padding(2.dp),
            verticalArrangement = Arrangement.Center, // 세로 가운데 정렬
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            displayedSpecialProducts.take(3).forEachIndexed { index, item ->
                SpecialItem(
                    item = item,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(
                            horizontal = screenWidth * 0.01f,
                            vertical = screenHeight * 0.005f
                        ),
                    isEven = (index % 2 == 0)
                )
            }
        }
    }
}