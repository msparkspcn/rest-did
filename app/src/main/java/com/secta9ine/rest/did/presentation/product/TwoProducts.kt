package com.secta9ine.rest.did.presentation.product

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
    productList: List<Product>
) {
    var displayedProducts by remember { mutableStateOf(productList.take(2)) }
    var productIndex by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        if(productList.size<3) {
            displayedProducts = productList
        } else {
            while(true) {
                delay(5000)
                Log.d(TAG,"productIndex:$productIndex, displayedProducts:$displayedProducts")
                productIndex = (productIndex + 2) % productList.size
                displayedProducts = productList.subList(productIndex,
                    minOf(productIndex + 2, productList.size))
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(2.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        displayedProducts.chunked(2).forEach {rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                rowItems.forEach { item ->
                    Item(item, Modifier.weight(1f))
                }
            }

        }
    }
}



