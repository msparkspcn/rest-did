package com.secta9ine.rest.did.presentation.order

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.secta9ine.rest.did.domain.model.OrderStatus

@Composable
fun OrderStatusCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconMultiplier: Float,
    title: String,
    orders: List<OrderStatus?>,
    rowCount: Int,
    columnCount: Int,
    msgTextSize: TextUnit,
    statusSize: TextUnit,
    iconSizeDp: Dp
) {
    Card(
        backgroundColor = Color.White,
        elevation = 2.dp,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(iconSizeDp * iconMultiplier)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = title,
                    fontSize = statusSize,
                    fontWeight = FontWeight.Bold
                )
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                color = Color(0xFFDEE2E6),
                thickness = 2.dp
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(top = 8.dp)
            ) {
                OrderGrid(
                    orders = orders,
                    rowCount = rowCount,
                    columnCount = columnCount,
                    msgTextSize = msgTextSize,
                    placeholderColor = Color.White
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 1000,
    heightDp = 600
)
@Composable
fun PreviewOrderStatusCard() {
    val dummyOrders = List(6) { index ->
        OrderStatus(orderNoC = "${index + 100}")
    }

    OrderStatusCard(
        icon = Icons.Default.Star, // 적절한 아이콘으로 교체 가능
        iconMultiplier = 1f,
        title = "주문 상태",
        orders = dummyOrders,
        rowCount = 2,
        columnCount = 3,
        msgTextSize = 14.sp,
        statusSize = 18.sp,
        iconSizeDp = 24.dp
    )
}
