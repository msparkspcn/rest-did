package com.secta9ine.rest.did.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier

@Composable
fun UncheckedBox(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(28.dp)) {
        // Draw the outer circle
        drawCircle(
            color = Color.Transparent, // No fill color
            radius = 13f,
            center = center,
            style = Stroke(width = 2f)
        )

        // Draw the cross line (unchecked)
        drawLine(
            start = center.copy(x = center.x - 5.704f, y = center.y), // (8.296, 13.786)
            end = center.copy(x = center.x + 1.074f, y = center.y + 3.844f), // (12.37, 17.63)
            color = Color(0xFFCFD4D8),
            strokeWidth = 2f,
            cap = Stroke.DefaultCap // round cap
        )
        drawLine(
            start = center.copy(x = center.x + 1.074f, y = center.y + 3.844f), // (12.37, 17.63)
            end = center.copy(x = center.x + 6.334f, y = center.y - 3.63f), // (19.704, 10.37)
            color = Color(0xFFCFD4D8),
            strokeWidth = 2f,
            cap = Stroke.DefaultCap // round cap
        )
    }
}