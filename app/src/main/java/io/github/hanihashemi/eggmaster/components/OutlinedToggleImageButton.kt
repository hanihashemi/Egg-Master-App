package io.github.hanihashemi.eggmaster.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.hanihashemi.eggmaster.R
import io.github.hanihashemi.eggmaster.ui.theme.Dimens
import io.github.hanihashemi.eggmaster.ui.theme.EggMasterTheme
import io.github.hanihashemi.eggmaster.ui.theme.InactiveColor
import io.github.hanihashemi.eggmaster.util.VibrateUtil

@Composable
fun OutlinedToggleImageButton(
    modifier: Modifier = Modifier,
    text: String,
    @DrawableRes iconId: Int,
    isSelect: Boolean,
    onClick: () -> Unit,
) {
    val color = if (isSelect) Color.White else InactiveColor
    val colorMatrix = ColorMatrix().apply {
        if (!isSelect) {
            setToSaturation(0f)
        }
    }
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
            .width(100.dp)
            .clip(CircleShape)
            .border(BorderStroke(1.dp, color), CircleShape)
            .clickable(interactionSource, null) { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            Modifier.padding(vertical = Dimens.PaddingSmall),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                modifier = Modifier.size(80.dp),
                painter = painterResource(id = iconId),
                contentDescription = "Boil type",
                colorFilter = ColorFilter.colorMatrix(colorMatrix)
            )
            Text(
                modifier = Modifier.padding(Dimens.PaddingXXSmall),
                text = text,
                maxLines = 1,
                overflow = TextOverflow.Clip,
                color = color,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
@Preview
fun OutlinedToggleImageButtonSelectedPreview() {
    EggMasterTheme {
        OutlinedToggleImageButton(
            isSelect = true,
            text = "Medium",
            iconId = R.drawable.ic_egg_boiled_medium,
            onClick = { }
        )
    }
}

@Composable
@Preview
fun OutlinedToggleImageButtonNotSelectedPreview() {
    EggMasterTheme {
        OutlinedToggleImageButton(
            isSelect = false,
            text = "Medium",
            iconId = R.drawable.ic_egg_boiled_medium,
            onClick = { }
        )
    }
}