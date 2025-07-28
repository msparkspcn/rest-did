package com.secta9ine.rest.did.presentation.product

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.secta9ine.rest.did.domain.model.ProductVo
import kotlinx.coroutines.delay

private const val TAG = "VerticalProductList"
@Composable
fun VerticalProductList(
    productList: List<ProductVo>,
    rollingYn: String,
) {
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
    val groupedProducts = fullProductList
        .filterNotNull()
        .groupBy { it.cornerCd }
    Column(
        modifier = Modifier.fillMaxHeight().background(Color.White)
    ) {
        groupedProducts.forEach { (_, products) ->
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Text(
                    text = products[0].cornerNm,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .background(Color(0xFF283237))
                        .fillMaxWidth()
                        .padding(10.dp)
                )

                products.chunked(8).forEach { rowItems ->
                    rowItems.forEach { item ->
                        VerticalItem(
                            item = item,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}