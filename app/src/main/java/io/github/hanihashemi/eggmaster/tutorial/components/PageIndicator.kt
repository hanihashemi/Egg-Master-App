package io.github.hanihashemi.eggmaster.tutorial.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.hanihashemi.eggmaster.ui.theme.PrimaryColor

private const val CIRCLE_SIZE = 10

@Composable
fun PageIndicator(
    modifier: Modifier,
    currentPage: Int,
    pageSize: Int = 3,
) {

    Row(
        modifier = modifier,
        verticalAlignment = CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            space = 5.dp,
            alignment = CenterHorizontally,
        )
    ) {

        for (i in 0 until pageSize) {
            val size by animateDpAsState(
                targetValue = if (i == currentPage) 12.dp else CIRCLE_SIZE.dp, label = ""
            )
            Circle(
                Modifier.size(size),
                color = if (i == currentPage) Color.White else PrimaryColor
            )
        }

    }
}

@Composable
private fun Circle(
    modifier: Modifier,
    color: Color,
) {
    Canvas(modifier) {
        drawCircle(color)
    }
}
