package com.secta9ine.rest.did.presentation.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.secta9ine.rest.did.domain.model.OrderStatus

@Composable
fun OrderGrid(
    orders: List<OrderStatus?>,
    rowCount: Int,
    columnCount: Int,
    msgTextSize: TextUnit,
    placeholderColor: Color = Color.White,
    modifier: Modifier = Modifier
) {
    val paddedOrders = orders.toMutableList()
    val totalCells = rowCount * columnCount
    while (paddedOrders.size < totalCells) {
        paddedOrders.add(null)
    }

    Column(modifier = modifier
        .fillMaxHeight()) {
        for (row in 0 until rowCount) {
            Row(
                modifier = Modifier
                    .weight(1f) // 균등 분할
                    .defaultMinSize(minHeight = msgTextSize.value.dp * 1.8f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (col in 0 until columnCount) {
                    val index = row * columnCount + col
                    val order = paddedOrders.getOrNull(index)

                    val align = when (col) {
                        0 -> TextAlign.Start
                        1 -> TextAlign.Center
                        else -> TextAlign.End
                    }
                    Text(
                        text = order?.orderNoC ?: "5555",
                        fontSize = msgTextSize,
                        modifier = Modifier.weight(1f).padding(4.dp, 0.dp),
                        textAlign = align,
                        color = if (order == null) placeholderColor else Color.Unspecified,
                        maxLines = 1
                    )
                }
            }
        }
    }
}