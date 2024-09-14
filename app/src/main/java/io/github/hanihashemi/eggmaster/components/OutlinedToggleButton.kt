package io.github.hanihashemi.eggmaster.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.hanihashemi.eggmaster.ui.theme.Dimens
import io.github.hanihashemi.eggmaster.ui.theme.EggMasterTheme
import io.github.hanihashemi.eggmaster.ui.theme.InactiveColor

@Composable
fun OutlinedToggleButton(
    modifier: Modifier = Modifier,
    isSelect: Boolean,
    text: String,
    onClick: () -> Unit,
) {
    val color = if (isSelect) Color.White else InactiveColor

    Box(
        modifier = modifier
            .clip(CircleShape)
            .border(BorderStroke(1.dp, color), CircleShape)
            .clickable { onClick() }
            .padding(Dimens.PaddingXSmall),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier.padding(Dimens.PaddingXSmall),
            text = text,
            color = color,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview
@Composable
fun OutlinedToggleButtonSelectedPreview() {
    EggMasterTheme {
        OutlinedToggleButton(
            isSelect = true,
            text = "Click me",
            onClick = { }
        )
    }
}

@Preview
@Composable
fun OutlinedToggleButtonNotSelectedPreview() {
    EggMasterTheme {
        OutlinedToggleButton(
            isSelect = false,
            text = "Click me",
            onClick = { }
        )
    }
}