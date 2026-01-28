package com.secta9ine.rest.did.presentation.product

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
fun Item2(
    item: ProductVo,
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
            width < 400.dp -> 40.sp
            width < 700.dp -> 43.sp
            width < 1300.dp -> 70.sp
            else -> 10.sp
        }
        val textSizeProductNm = when {
            width < 200.dp -> 11.sp
            width < 400.dp -> 30.sp
            width < 700.dp -> 33.sp
            width < 1300.dp -> 60.sp
            else -> 9.sp
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.65f)
            ) {
                BoxWithConstraints(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val maxBoxWidth = maxWidth
                    val maxBoxHeight = maxHeight

                    val imageWidth = maxBoxWidth
                    val imageHeight = imageWidth * 4/ 5

                    val finalImageHeight = if (imageHeight <= maxBoxHeight) imageHeight else maxBoxHeight
                    val finalImageWidth = if (imageHeight <= maxBoxHeight) imageWidth else maxBoxHeight * 5f / 4f

                    Box(
                        modifier = Modifier
                            .size(finalImageWidth, finalImageHeight)
                            .align(Alignment.TopCenter)
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

            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .weight(0.35f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = item.itemNm,
                    fontSize = textSizeProductNm,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "${item.price}".formatCurrency() ?: "0",
                    fontSize = textSizePrice,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xFF1EB5EC),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }

        }
    }
}