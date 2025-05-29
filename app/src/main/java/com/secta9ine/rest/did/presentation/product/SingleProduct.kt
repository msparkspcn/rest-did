package com.secta9ine.rest.did.presentation.product

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
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

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    Log.d(TAG,"screenWidth:$screenWidth")
    val density = LocalDensity.current
    val itemNmSize = with(density) { (screenWidth * 0.05f).toSp() }
    val itemNmEnSize = with(density) { (screenWidth * 0.02f).toSp() }
    val priceSize = with(density) { (screenWidth * 0.1f).toSp() }
    val unitSize = with(density) { (screenWidth * 0.07f).toSp() }
    LaunchedEffect(productList) {
        Log.d(TAG,"productList 변경")
        // 새로운 productList가 들어올 때마다 초기화
        productIndex = 0
        displayedProducts = productList.take(1)
    }

    LaunchedEffect(productList, rollingYn) {
        Log.d(TAG,"productList, rollingYn 변경")
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
                        .weight(4f)
                ) {
                    Text(
                        text = item.itemNm,
                        fontSize = itemNmSize,
                        fontWeight = FontWeight.Bold,
                    )
                    item.itemNmEn?.let {
                        Text(
                            text = it,
                            fontSize = itemNmEnSize,
                            color = Color(0xFF1BAAFE)
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
                            fontSize = priceSize,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1BAAFE)
                        )
                        Text(
                            text = "원",
                            fontSize = unitSize,
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
                        .weight(6f)
                        .fillMaxHeight()
                        .background((Color.White))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(3f / 2f)
                            .align(Alignment.Center)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(item.imgPath)
                                .crossfade(true)
                                .build(),
                            contentDescription = "content",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .fillMaxWidth()
//                                .height(imageHeight)
                        )
                    }
                }
            }
        }
    }
}

