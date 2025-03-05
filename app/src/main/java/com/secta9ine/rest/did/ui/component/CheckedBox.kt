package com.secta9ine.rest.did.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun CheckedBox() {
    Canvas(modifier = Modifier.size(28.dp)) {
        drawCircle(color = Color(0xFF1BAAFE), radius = 14f, center = center) // Circle
        drawLine(
            start = center.copy(x = center.x - 5.704f, y = center.y), // (8.296, 13.786)
            end = center.copy(x = center.x + 1.074f, y = center.y + 3.844f), // (12.37, 17.63)
            color = Color.White,
            strokeWidth = 2f,
            cap = Stroke.DefaultCap // round cap
        )
        drawLine(
            start = center.copy(x = center.x + 1.074f, y = center.y + 3.844f), // (12.37, 17.63)
            end = center.copy(x = center.x + 6.334f, y = center.y - 3.63f), // (19.704, 10.37)
            color = Color.White,
            strokeWidth = 2f,
            cap = Stroke.DefaultCap // round cap
        )
    }
}