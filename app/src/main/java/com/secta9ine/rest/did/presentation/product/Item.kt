package com.secta9ine.rest.did.presentation.product

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.secta9ine.rest.did.domain.model.Product
import com.secta9ine.rest.did.util.formatCurrency

@Composable
fun Item(
    item: Product,
    modifier: Modifier
) {
    var itemWidth by remember { mutableStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(4.dp)
            .onGloballyPositioned { layoutCoordinates ->
                itemWidth = layoutCoordinates.size.width
            }

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .background((Color.White))
        ) {
            Image(
                painter = rememberAsyncImagePainter("http://o2pos.spcnetworks.kr/files/pos/2022/04/04/1110/tmb_product_00F144.png"),
                contentDescription = "content",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        // 아이템의 너비에 따라 텍스트 크기 조정
        val textSizeProductNm = when {
            itemWidth < 200 -> 11.sp // 아이템 너비가 200dp 미만일 때
            itemWidth < 400 -> 13.sp // 아이템 너비가 200dp 이상 300dp 미만일 때
            else -> 30.sp // 아이템 너비가 300dp 이상일 때
        }

        val textSizeProductEngNm = when {
            itemWidth < 200 -> 8.sp
            itemWidth < 400 -> 10.sp
            else -> 15.sp
        }

        val textSizePrice = when {
            itemWidth < 200 -> 12.sp
            itemWidth < 400 -> 15.sp
            else -> 50.sp
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .border(2.dp, Color.White, RoundedCornerShape(16.dp)),

            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Top)
                    .padding(10.dp)
                    .fillMaxWidth(0.65f)
            ) {
                Text(
                    text = item.productNm,
                    fontSize = textSizeProductNm,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
                item.productEngNm?.let {
                    Text(
                        text = it,
                        fontSize = textSizeProductEngNm,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Text(
                modifier = Modifier
                    .align(Alignment.Top)
                    .padding(10.dp)
                    .fillMaxHeight()
                    .wrapContentWidth(Alignment.End), // 오른쪽 정렬,
                text = "${item.price}".formatCurrency() ?: "0",
                fontSize = textSizePrice,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Magenta
            )
        }
    }
}