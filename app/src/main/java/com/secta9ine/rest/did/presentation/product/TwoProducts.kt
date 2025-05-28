package com.secta9ine.rest.did.presentation.product

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.secta9ine.rest.did.domain.model.Product
import kotlinx.coroutines.delay

private const val TAG = "TwoProducts"
@Composable
fun TwoProducts(
    productList: List<Product>,
    rollingYn: String,
) {
    var displayedProducts by remember { mutableStateOf(productList.take(2)) }
    var productIndex by remember { mutableStateOf(0) }

    LaunchedEffect(productList) {
        Log.d(TAG,"productList 변경")
        // 새로운 productList 가 들어올 때마다 초기화
        productIndex = 0
        displayedProducts = productList.take(2)
    }

    LaunchedEffect(productList, rollingYn) {
        Log.d(TAG,"productList, rollingYn 변경")
        if(productList.size>2&&rollingYn=="Y") {
            while(true) {
                delay(5000)
                val nextIndex = productIndex + 2

                if (nextIndex >= productList.size) {
                    productIndex = 0
                    displayedProducts = productList.take(2)
                } else {
                    displayedProducts = productList.subList(nextIndex, minOf(nextIndex + 2, productList.size))
                    productIndex = nextIndex
                }
                Log.d(TAG,"productIndex:$productIndex, displayedProducts:$displayedProducts")
            }
        }
    }

    Crossfade(targetState = displayedProducts) {products ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .padding(2.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            products.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    rowItems.forEach { item ->
                        key(item.itemCd) {
                            Item(item, Modifier.weight(1f))
                        }
                    }
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}



