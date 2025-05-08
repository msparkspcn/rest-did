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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
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
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    Log.d(TAG,"screenWidth:$screenWidth")
    val density = LocalDensity.current
    val productNmSize = with(density) { (screenWidth * 0.05f).toSp() }
    val productEngNmSize = with(density) { (screenWidth * 0.02f).toSp() }
    val priceSize = with(density) { (screenWidth * 0.1f).toSp() }
    val unitSize = with(density) { (screenWidth * 0.07f).toSp() }
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
                        .weight(4f)
                ) {
                    Text(
                        text = item.productNm,
                        fontSize = productNmSize,
                        fontWeight = FontWeight.Bold,
                    )
                    item.productEngNm?.let {
                        Text(
                            text = it,
                            fontSize = productEngNmSize,
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
//                        .border(
//                            width = 2.dp,
//                            color = Color(0xFF1BAAFE)
//                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
//                            .aspectRatio(5f / 4f)
                            .align(Alignment.Center) // 이미지 컨테이너를 중앙에 배치
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(item.imgPath),
                            contentDescription = "content",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.FillBounds
                        )
                    }

                }
            }
        }

    }
}

