package com.secta9ine.rest.did.presentation.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.secta9ine.rest.did.domain.model.ProductVo
import kotlinx.coroutines.delay

private const val TAG = "VerticalProductList"
@Composable
fun VerticalProductList(
    productList: List<ProductVo>,
    rollingYn: String,
) {
    val groupedChunks = productList
        .groupBy { it.cornerCd }
        .mapValues { (_, list) -> list.chunked(8) }

    val maxPageCount = groupedChunks.values.maxOfOrNull { it.size } ?: 1
    var currentPage by remember { mutableStateOf(0) }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    LaunchedEffect(productList, rollingYn) {
        if (rollingYn == "Y" && maxPageCount > 1) {
            while (true) {
                delay(5000)
                currentPage = (currentPage + 1) % maxPageCount
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxHeight().background(Color.White)
    ) {
        groupedChunks.forEach { (_, chunkedList) ->
            val pageItems = chunkedList.getOrNull(currentPage).orEmpty()
            val cornerNm = pageItems.firstOrNull()?.cornerNm ?: ""
            if (pageItems.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = cornerNm,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier
                            .background(Color(0xFF283237))
                            .fillMaxWidth()
                            .padding(10.dp)
                    )

                    pageItems.forEach { rowItems ->
                        VerticalItem(
                            item = rowItems,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    repeat(8 - pageItems.size) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(screenHeight * 0.05f)
                        )
                    }
                }
            }
        }
    }
}