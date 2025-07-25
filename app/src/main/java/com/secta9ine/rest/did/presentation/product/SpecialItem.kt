package com.secta9ine.rest.did.presentation.product

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.secta9ine.rest.did.R
import com.secta9ine.rest.did.domain.model.ProductVo
import com.secta9ine.rest.did.util.formatCurrency

private const val TAG = "SpecialItem"
@Composable
fun SpecialItem(
    item: ProductVo,
    modifier: Modifier,
    isEven: Boolean
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .clip(RoundedCornerShape(8.dp))
            .border(2.dp, Color.White, RoundedCornerShape(16.dp))
            .padding(15.dp, 5.dp)
    ) {

        val width = maxWidth
        Log.d(TAG,"itemWidth:$width, itemNmEn:${item.itemNmEn}, calorie:${item.calorie}" + "isEven:$isEven")
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.White)
                .clip(RoundedCornerShape(8.dp))
                .border(2.dp, Color.White, RoundedCornerShape(16.dp))
                .padding(15.dp, 5.dp)


        ) {

            val textSizePrice = when {
                width < 200.dp -> 12.sp
                width < 400.dp -> 14.sp
                width < 800.dp -> 30.sp
                width > 1200.dp -> 16.sp
                else -> 50.sp
            }

            val textSizeProductNm = when {
                width < 200.dp -> 10.sp
                width < 400.dp -> 12.sp
                width < 800.dp -> 28.sp
                width > 1200.dp -> 14.sp
                else -> 33.sp
            }

            val textSizeExpln = when {
                width < 200.dp -> 7.sp
                width < 400.dp -> 9.sp
                width < 800.dp -> 22.sp
                width > 1200.dp -> 12.sp
                else -> 20.sp
            }

            val textSizeKcal = when {
                width < 200.dp -> 6.sp
                width < 400.dp -> 8.sp
                width < 800.dp -> 22.sp
                width > 1200.dp -> 10.sp
                else -> 18.sp
            }

            val textSizeProductEngNm = when {
                width < 200.dp -> 4.sp
                width < 400.dp -> 6.sp
                width < 800.dp -> 20.sp
                width > 1200.dp -> 10.sp
                else -> 18.sp
            }

            if (isEven) {
                ItemMainInfo(
                    modifier = Modifier.fillMaxWidth(0.6f).fillMaxHeight(),
                    item = item,
                    textSizeProductNm = textSizeProductNm,
                    textSizeProductEngNm = textSizeProductEngNm
                )
                ItemSubInfo(
                    modifier = Modifier.fillMaxWidth(1f).fillMaxHeight(),
                    item = item,
                    textSizeExpln = textSizeExpln,
                    textSizePrice = textSizePrice,
                    textSizeKcal = textSizeKcal,
                    horizontalAlignment = Alignment.End
                )
            } else {
                ItemSubInfo(
                    modifier = Modifier.fillMaxWidth(0.4f).fillMaxHeight(),
                    item = item,
                    textSizeExpln = textSizeExpln,
                    textSizePrice = textSizePrice,
                    textSizeKcal = textSizeKcal
                )
                ItemMainInfo(
                    modifier = Modifier.fillMaxWidth(1f).fillMaxHeight(),
                    item = item,
                    textSizeProductNm = textSizeProductNm,
                    textSizeProductEngNm = textSizeProductEngNm,
                    horizontalAlignment = Alignment.End
                )
            }
        }
    }
}

@Composable
fun ItemMainInfo(
    modifier: Modifier,
    item: ProductVo,
    textSizeProductNm: TextUnit,
    textSizeProductEngNm: TextUnit,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.7f)
        ) {

            val imageHeight = remember(maxWidth) {
                maxWidth * 2 / 3
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
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
                        .height(imageHeight)
                )
            }
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth(),
            color = Color(0xFF1EB5EC),
            thickness = 4.dp
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f)
                .padding(10.dp),
            horizontalAlignment = horizontalAlignment
        ) {
            Text(
                text = item.itemNm,
                fontSize = textSizeProductNm,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            item.itemNmEn?.let {
                Text(
                    text = it,
                    fontSize = textSizeProductEngNm,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xFFAFB7BF),
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
fun ItemSubInfo(
    modifier: Modifier,
    item: ProductVo,
    textSizeExpln: TextUnit,
    textSizePrice: TextUnit,
    textSizeKcal: TextUnit,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.5f)
                .background((Color.White))
        ) {
            Image(
                painter = painterResource(id = R.drawable.tag_new),
                contentDescription = "content",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
        item.productExpln?.let {
            Text(
                text = it,
                fontSize = textSizeExpln,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f),
                color = Color(0xFFAFB7BF),
                fontWeight = FontWeight.Bold,
            )
        } ?: run {
            Spacer(modifier = Modifier.weight(0.2f))
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth(),
            color = Color(0xFF1EB5EC),
            thickness = 4.dp // 밑줄 두께
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f)
                .padding(7.dp),
            horizontalAlignment = horizontalAlignment
        ) {
            Text(
                text = "${item.price}".formatCurrency() ?: "0",
                fontSize = textSizePrice,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color(0xFF1EB5EC)
            )
            item.calorie?.let { calorie ->
                Text(
                    text = "(${calorie.formatCurrency()} kcal)",
                    fontSize = textSizeKcal,
                    color = Color(0xFFAFB7BF),
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}
