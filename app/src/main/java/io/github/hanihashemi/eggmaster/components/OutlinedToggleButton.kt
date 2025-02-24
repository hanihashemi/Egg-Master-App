package io.github.hanihashemi.eggmaster.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.hanihashemi.eggmaster.ui.theme.Dimens
import io.github.hanihashemi.eggmaster.ui.theme.EggMasterTheme
import io.github.hanihashemi.eggmaster.ui.theme.InactiveColor
import io.github.hanihashemi.eggmaster.util.VibrateUtil

@Composable
fun OutlinedToggleButton(
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(Dimens.PaddingXSmall),
    isSelect: Boolean,
    text: String,
    onClick: () -> Unit,
    smallFontSizes: Boolean = false,
    shape: Shape = CircleShape,
) {
    val color = if (isSelect) Color.White else InactiveColor
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                }

                is PressInteraction.Release -> {
                    VibrateUtil.vibrate(context)
                }

                is PressInteraction.Cancel -> {
                }
            }
        }
    }

    Box(
        modifier = modifier
            .clip(shape)
            .border(BorderStroke(1.dp, color), shape)
            .clickable(interactionSource, null) { onClick() }
            .padding(padding),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier.padding(padding),
            text = text,
            color = color,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = if (smallFontSizes) MaterialTheme.typography.bodySmall else MaterialTheme.typography.bodyLarge,
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

@Preview
@Composable
fun OutlinedToggleButtonLeftAndRightPreview() {
    EggMasterTheme {
        Row {
            OutlinedToggleButton(
                isSelect = true,
                padding = PaddingValues(Dimens.PaddingXXXSmall),
                text = "Click me",
                shape = RoundedCornerShape(50.dp, 0.dp, 0.dp, 50.dp),
                onClick = { },
                smallFontSizes = true,
            )
            OutlinedToggleButton(
                isSelect = false,
                padding = PaddingValues(Dimens.PaddingXXXSmall),
                text = "Click me",
                shape = RoundedCornerShape(0.dp, 50.dp, 50.dp, 0.dp),
                onClick = { },
                smallFontSizes = true,
            )
        }
    }
}
