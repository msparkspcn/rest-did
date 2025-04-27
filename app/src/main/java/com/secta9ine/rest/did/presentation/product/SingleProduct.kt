package com.secta9ine.rest.did.presentation.product

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Divider
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.secta9ine.rest.did.domain.model.Product
import com.secta9ine.rest.did.util.formatCurrency
import kotlinx.coroutines.delay

private const val TAG = "SingleProduct"
@Composable
fun SingleProduct(
    productList: List<Product>,
    rollingYn: String,
) {
    var displayedProducts by remember { mutableStateOf(productList.take(1)) }
    var productIndex by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        if(productList.size>1&&rollingYn=="Y") {
            while(true) {
                delay(5000)
                Log.d(TAG,"productIndex:$productIndex, displayedProducts:$displayedProducts")
                productIndex = (productIndex + 1) % productList.size
                displayedProducts = productList.subList(productIndex,
                    minOf(productIndex + 1, productList.size))
            }
        }
    }
    displayedProducts.chunked(1).forEach { rowItems ->
        rowItems.forEach { item ->
            Row {
                Column(
                    modifier = Modifier
                        .padding(20.dp, 20.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = item.productNm,
                        fontSize = 50.sp
                    )
                    item.productEngNm?.let {
                        Text(
                            text = it,
                            fontSize = 25.sp
                        )
                    }
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 8.dp),
                        color = Color(0xFF1BAAFE),
                        thickness = 2.dp // 밑줄 두께
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.End,
                    ) {
                        Text(
                            text = "${item.price}".formatCurrency() ?: "0",
                            fontSize = 70.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1BAAFE)
                        )
                        Text(
                            text = "원",
                            fontSize = 50.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Divider(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 8.dp),
                        color = Color(0xFF1BAAFE),
                        thickness = 2.dp // 밑줄 두께
                    )

                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(5f / 4f)
                        .background((Color.White))
                        .border(
                            width = 2.dp,
                            color = Color(0xFF1BAAFE)
                        )
                ) {
                    Image(
                        painter = rememberAsyncImagePainter("http://o2pos.spcnetworks.kr/files/pos/2022/04/04/1110/tmb_product_00F144.png"),
                        contentDescription = "content",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }

    }
}

