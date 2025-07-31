package com.secta9ine.rest.did.presentation.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.secta9ine.rest.did.domain.model.ProductVo

private const val TAG = "VerticalProductList"
@Composable
fun VerticalProductList(
    productList: List<ProductVo>
) {
    val groupedProducts = productList.groupBy { it.cornerCd }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val singleItemEstimatedHeight = screenHeight * 0.05f // VerticalItem 하나의 대략적인 높이

    val maxDisplayCount = 15

    Column(
        modifier = Modifier.fillMaxHeight().background(Color.White)
    ) {
        val totalProductsToShow = groupedProducts.values.sumOf { it.size }
        val displayedProductCount = minOf(totalProductsToShow, maxDisplayCount)

        val baseTotalWeight = groupedProducts.size
        val totalActualWeight = groupedProducts.values.sumOf { products -> minOf(products.size, maxDisplayCount) } // 실제 아이템 수에 비례한 가중치 합

        groupedProducts.forEach { (cornerCd, productsInCorner) ->
            val cornerNm = productsInCorner.firstOrNull()?.cornerNm ?: ""
            val itemsToDisplayInCorner = productsInCorner.take(maxDisplayCount)
            val currentCornerItemCount = itemsToDisplayInCorner.size
            if (itemsToDisplayInCorner.isNotEmpty()) {
                val cornerWeight = if (displayedProductCount > 0) {
                    currentCornerItemCount.toFloat() / displayedProductCount.toFloat()
                } else {
                    1f / groupedProducts.size.toFloat()
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(cornerWeight)
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

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = true)
                    ) {
                        itemsToDisplayInCorner.forEach { item ->
                            VerticalItem(
                                item = item,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(singleItemEstimatedHeight)
                            )
                        }
                    }
                }
            }
        }
    }
}