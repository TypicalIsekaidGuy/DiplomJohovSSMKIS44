package com.johov.bitcoin.ui.view.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun DashedLine(modifier:Modifier = Modifier, color: Color = Color(0xFF524B5B)) {
    Canvas(modifier.fillMaxWidth().height(4.dp)) {

        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = pathEffect,
            strokeWidth = 2.dp.toPx()
        )
    }
}