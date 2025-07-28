package com.secta9ine.rest.did.presentation.product

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.secta9ine.rest.did.domain.model.ProductVo
import com.secta9ine.rest.did.util.formatCurrency

private const val TAG = "VerticalItem"
@Composable
fun VerticalItem(
    item: ProductVo,
    modifier: Modifier
) {
    Log.d(TAG,"item:${item}")
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    Box(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                val y = size.height - strokeWidth / 2
                drawLine(
                    color = Color.LightGray,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = strokeWidth
                )
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp, 5.dp)
                .background(Color.White)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.imgPath)
                    .crossfade(true)
                    .build(),
                contentDescription = "content",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .width(screenWidth * 0.1f)
                    .aspectRatio(3f / 2f)
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = item.itemNm,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${item.price}".formatCurrency() ?: "0",
                        color = Color(0xFF1EB5EC),
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = item.itemNmEn!!,
                    color = Color(0xFFAFB7BF),
                )
            }
        }
    }
}