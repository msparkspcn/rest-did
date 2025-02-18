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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.secta9ine.rest.did.R
import com.secta9ine.rest.did.util.formatCurrency

@Composable
fun TwoProducts() {
    val itemList = listOf(
        Item(
            productNm = "탐라 흑돼지 김치찌개",
            productEngNm = "Tamra BlackPork Kimchi Jjigae",
            price = 7500.toString().formatCurrency() ?: "0"
        ),
        Item(
            productNm = "제주 고기국수",
            productEngNm = "Jeju Meat Noodles",
            price = 8500.toString().formatCurrency() ?: "0"
        ),
        Item(
            productNm = "제주 흑돼지 갈비",
            productEngNm = "Jeju Black Pork Ribs",
            price = 9500.toString().formatCurrency() ?: "0"
        ),
        Item(
            productNm = "올레길 비빔밥",
            productEngNm = "Olleh Trail Bibimbap",
            price = 12000.toString().formatCurrency() ?: "0"
        ),
        Item(
            productNm = "한라산 백숙",
            productEngNm = "Hallasan Chicken Soup",
            price = 13500.toString().formatCurrency() ?: "0"
        ),
        Item(
            productNm = "제주 감귤 빙수",
            productEngNm = "Jeju Tangerine Bingsu",
            price = 6000.toString().formatCurrency() ?: "0"
        ),
        Item(
            productNm = "제주도 전복죽",
            productEngNm = "Jeju Abalone Porridge",
            price = 18000.toString().formatCurrency() ?: "0"
        ),
        Item(
            productNm = "검은돼지 스테이크",
            productEngNm = "Black Pig Steak",
            price = 22000.toString().formatCurrency() ?: "0"
        ),
        Item(
            productNm = "제주 연어회",
            productEngNm = "Jeju Salmon Sashimi",
            price = 17000.toString().formatCurrency() ?: "0"
        ),
        Item(
            productNm = "서귀포 한우 갈비찜",
            productEngNm = "Seogwipo Hanwoo Braised Ribs",
            price = 25000.toString().formatCurrency() ?: "0"
        )
    )


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(2.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        itemList.chunked(2).forEach {rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                rowItems.forEach { item ->
                    Product(item, Modifier.weight(1f))
                }
            }

        }
    }
}

@Composable
fun Product(
    item: Item,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .background((Color.White))
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "content",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
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
            ) {
                Text(
                    text = item.productNm,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines =1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = item.productEngNm,
                    fontSize = 15.sp,
                    maxLines =1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                modifier = Modifier
                    .align(Alignment.Top)
                    .padding(10.dp, 0.dp),
                text ="${item.price}",
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Magenta
            )
        }
    }
}

data class Item(
    val productNm: String,
    val productEngNm: String,
    val price: String
)