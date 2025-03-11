package com.secta9ine.rest.did.presentation.product

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.secta9ine.rest.did.R
import com.secta9ine.rest.did.domain.model.Product
import com.secta9ine.rest.did.util.formatCurrency

private const val TAG = "SpecialItem"
@Composable
fun SpecialItem(
    item: Product,
    modifier: Modifier,
    isEven: Boolean
) {
    var itemWidth by remember { mutableStateOf(0) }
    Log.d(TAG,"itemWidth:$itemWidth, productEngNm:${item.productEngNm}, calorie:${item.calorie}" +
            "isEven:$isEven")
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .clip(RoundedCornerShape(8.dp))
            .border(2.dp, Color.White, RoundedCornerShape(16.dp))
            .padding(15.dp, 5.dp)
            .onGloballyPositioned { layoutCoordinates ->
                itemWidth = layoutCoordinates.size.width
            }

    ) {
        val textSizePrice = when {
            itemWidth < 200 -> 12.sp
            itemWidth < 400 -> 14.sp
            itemWidth < 800 -> 30.sp
            itemWidth > 1200 -> 16.sp
            else -> 50.sp
        }

        val textSizeProductNm = when {
            itemWidth < 200 -> 10.sp
            itemWidth < 400 -> 12.sp
            itemWidth < 800 -> 28.sp
            itemWidth > 1200 -> 14.sp
            else -> 33.sp
        }

        val textSizeExpln = when {
            itemWidth < 200 -> 7.sp
            itemWidth < 400 -> 9.sp
            itemWidth < 800 -> 22.sp
            itemWidth > 1200 -> 12.sp
            else -> 20.sp
        }

        val textSizeKcal = when {
            itemWidth < 200 -> 6.sp
            itemWidth < 400 -> 8.sp
            itemWidth < 800 -> 22.sp
            itemWidth > 1200 -> 10.sp
            else -> 18.sp
        }

        val textSizeProductEngNm = when {
            itemWidth < 200 -> 4.sp
            itemWidth < 400 -> 6.sp
            itemWidth < 800 -> 20.sp
            itemWidth > 1200 -> 10.sp
            else -> 18.sp
        }

        if(isEven) {
            ItemMainInfo(
                modifier = Modifier.fillMaxWidth(0.6f).fillMaxHeight(),
                item = item,
                textSizeProductNm = textSizeProductNm,
                textSizeProductEngNm = textSizeProductEngNm)
            ItemSubInfo(
                modifier = Modifier.fillMaxWidth(1f).fillMaxHeight(),
                item= item,
                textSizeExpln = textSizeExpln,
                textSizePrice = textSizePrice,
                textSizeKcal = textSizeKcal,
                horizontalAlignment = Alignment.End
            )
        }
        else {
            ItemSubInfo(
                modifier = Modifier.fillMaxWidth(0.4f).fillMaxHeight(),
                item= item,
                textSizeExpln = textSizeExpln,
                textSizePrice = textSizePrice,
                textSizeKcal = textSizeKcal)
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

@Composable
fun ItemMainInfo(
    modifier: Modifier,
    item: Product,
    textSizeProductNm: TextUnit,
    textSizeProductEngNm: TextUnit,
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
                .weight(0.7f) // 비율 적용
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    "http://o2pos.spcnetworks.kr/files/pos/2022/04/04/1110/tmb_product_00F144.png"),
                contentDescription = "content",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth(),
            color = Color(0xFF1BAAFE),
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
                text = item.productNm,
                fontSize = textSizeProductNm,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            item.productEngNm?.let {
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
    item: Product,
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
            color = Color(0xFF1BAAFE),
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
                color = Color(0xFF1BAAFE)
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
