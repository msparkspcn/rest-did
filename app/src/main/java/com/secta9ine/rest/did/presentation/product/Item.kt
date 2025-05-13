package com.secta9ine.rest.did.presentation.product

import android.util.Log
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.secta9ine.rest.did.R
import com.secta9ine.rest.did.domain.model.Product
import com.secta9ine.rest.did.util.formatCurrency
private const val TAG = "Item"
@Composable
fun Item(
    item: Product,
    modifier: Modifier
) {
    var itemWidth by remember { mutableStateOf(0) }
    Log.d(TAG,"itemWidth:$itemWidth")
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
                contentScale = ContentScale.Fit
            )
        }
        val textSizePrice = when {
            itemWidth < 200 -> 12.sp
            itemWidth < 400 -> 15.sp
            itemWidth < 700 -> 30.sp
            itemWidth < 1300 -> 50.sp
            else -> 10.sp
        }
        val textSizeProductNm = when {
            itemWidth < 200 -> 11.sp
            itemWidth < 400 -> 13.sp
            itemWidth < 700 -> 28.sp
            itemWidth < 1300 -> 45.sp
            else -> 9.sp
        }

        val textSizeKcal = when {
            itemWidth < 200 -> 8.sp
            itemWidth < 400 -> 10.sp
            itemWidth < 700 -> 18.sp
            itemWidth < 1300 -> 28.sp
            else -> 6.sp
        }

        val textSizeProductEngNm = when {
            itemWidth < 200 -> 8.sp
            itemWidth < 400 -> 10.sp
            itemWidth < 700 -> 18.sp
            itemWidth < 1300 -> 28.sp
            else -> 6.sp
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
                        modifier = Modifier.fillMaxWidth()
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
                        color = Color(0xFF1BAAFE)
                    )
                    Text(
                        text = stringResource(R.string.currency_unit),
                        fontSize = textSizeKcal,
                        fontWeight = FontWeight.Bold
                    )
                }

                item.calorie?.let { calorie ->
                    Text(
                        text = "(${calorie.formatCurrency()} kcal)",
                        fontSize = textSizeKcal,
                        color = Color(0xFFAFB7BF),
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                    )
                }
            }
        }
    }
}