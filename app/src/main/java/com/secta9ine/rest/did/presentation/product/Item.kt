package com.secta9ine.rest.did.presentation.product

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.secta9ine.rest.did.R
import com.secta9ine.rest.did.domain.model.ProductVo
import com.secta9ine.rest.did.util.formatCurrency
private const val TAG = "Item"
@Composable
fun Item(
    item: ProductVo,
    showNutrition: Boolean = false,
    modifier: Modifier
) {
    Log.d(TAG,"itemNm:${item.itemNm}")
    BoxWithConstraints(
        modifier = modifier
            .fillMaxHeight()
            .padding(4.dp)
    ) {
        val width = maxWidth
        Log.d(TAG,"1width:$width")
        val textSizePrice = when {
            width < 200.dp -> 12.sp
            width < 400.dp -> 25.sp
            width < 700.dp -> 30.sp
            width < 1300.dp -> 60.sp
            else -> 10.sp
        }
        val textSizeProductNm = when {
            width < 200.dp -> 11.sp
            width < 400.dp -> 23.sp
            width < 700.dp -> 28.sp
            width < 1300.dp -> 45.sp
            else -> 9.sp
        }

        val textSizeProductEngNm = when {
            width < 200.dp -> 8.sp
            width < 400.dp -> 16.sp
            width < 700.dp -> 25.sp
            width < 1300.dp -> 28.sp
            else -> 6.sp
        }
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(
                        if (showNutrition) 0.6f else 0.8f
                    )
                    .background((Color.White))
            ) {
                BoxWithConstraints(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val maxBoxWidth = maxWidth
                    val maxBoxHeight = maxHeight

                    val imageWidth = maxBoxWidth
                    val imageHeight = imageWidth * 2 / 3

                    val finalImageHeight = if (imageHeight <= maxBoxHeight) imageHeight else maxBoxHeight
                    val finalImageWidth = if (imageHeight <= maxBoxHeight) imageWidth else maxBoxHeight * 3f / 2f

                    Box(
                        modifier = Modifier
                            .size(finalImageWidth, finalImageHeight)
                            .align(Alignment.Center)
                            .background(Color.White)
                            .padding(10.dp)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(
                                    if (item.imgPath.isNullOrBlank()) {
                                        R.drawable.no_image
                                    } else {
                                        item.imgPath
                                    }
                                )
                                .crossfade(true)
                                .build(),
                            contentDescription = "content",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
//                            .height(imageHeight)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(
                        if (showNutrition) 0.4f else 0.2f
                    )
                    .clip(
                        RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 0.dp,
                            bottomStart = 8.dp,
                            bottomEnd = 8.dp
                        )
                    )
                    .background(Color.White),
//                    .border(2.dp, Color.White, RoundedCornerShape(16.dp)),

                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Top)
                        .padding(10.dp)
                        .fillMaxWidth(0.65f)
                ) {
                    Text(
                        text = item.itemNm,
                        fontSize = textSizeProductNm,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                    item.itemNmEn?.let {
                        Text(
                            text = it,
                            fontSize = textSizeProductEngNm,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0xFF444444)
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.Top)
                        .padding(8.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.End,
                    ) {
                        Text(
                            text = "${item.price}".formatCurrency() ?: "0",
                            fontSize = textSizePrice,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color(0xFF1EB5EC)
                        )
                    }
                }
            }
        }
    }
}