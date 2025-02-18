package com.secta9ine.rest.did.presentation.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.secta9ine.rest.did.util.formatCurrency

@Composable
fun ProductList() {
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
            .background(Color(0xFFE0E0E0))
            .padding(8.dp),
        verticalArrangement = Arrangement.Center, // 세로 가운데 정렬
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemList.take(8).chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                rowItems.forEach { item ->
                    Product(
                        item = item,
                        modifier = Modifier
                            .fillMaxWidth(0.25f)
                            .fillMaxHeight(0.25f)
                    )
                }
            }
        }
    }
}