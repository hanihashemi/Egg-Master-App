package io.github.hanihashemi.eggmaster.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// TODO: use this shapes
val Shapes = Shapes(
    small = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 12.dp,
        bottomStart = 12.dp,
        bottomEnd = 0.dp
    ),
    medium = RoundedCornerShape(
        topStart = 12.dp,
        topEnd = 12.dp,
        bottomStart = 12.dp,
        bottomEnd = 12.dp
    ),
    large = RoundedCornerShape(32.dp),
)
