package io.github.hanihashemi.eggmaster.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.hanihashemi.eggmaster.ui.theme.Dimens
import io.github.hanihashemi.eggmaster.ui.theme.Shapes
import io.github.hanihashemi.eggmaster.ui.theme.Typography
import io.github.hanihashemi.eggmaster.util.VibrateUtil

@Composable
fun Button(
    modifier: Modifier = Modifier,
    text: String,
    style: ButtonStyle = ButtonDefaultStyles.Primary,
    onClick: () -> Unit = {},
) {
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
            .fillMaxWidth()
            .clip(Shapes.large)
            .border(BorderStroke(style.borderWidth, style.borderColor), CircleShape)
            .background(color = style.backgroundColor)
            .clickable(interactionSource, null) { onClick() }
            .padding(Dimens.PaddingNormal),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = style.textColor,
            style = style.textStyle,
        )
    }
}

object ButtonDefaultStyles {
    val Primary = ButtonStyle(
        backgroundColor = Color(0xFF2C2D5C),
        textColor = Color(0xFFFFFFFF),
    )

    val Secondary = ButtonStyle(
        backgroundColor = Color(0xFFFFFFFF),
        textColor = Color(0xFF2C2D5C),
    )

    val Outline = ButtonStyle(
        backgroundColor = Color(0x00E5E5E5),
        textColor = Color(0xFFFFFFFF),
        borderColor = Color(0xFFFFFFFF),
        borderWidth = 2.dp,
    )

    val Transparent = ButtonStyle(
        backgroundColor = Color.Transparent,
        textColor = Color(0xFF949FDA),
        textStyle = Typography.bodySmall,
    )
}

data class ButtonStyle(
    val backgroundColor: Color,
    val textColor: Color,
    val borderColor: Color = Color.Transparent,
    val borderWidth: Dp = 0.dp,
    val textStyle: TextStyle = Typography.displaySmall,
)

@Preview
@Composable
fun ButtonPrimaryPreview() {
    Button(text = "Next")
}

@Preview
@Composable
fun ButtonSecondaryPreview() {
    Button(
        text = "Next",
        style = ButtonDefaultStyles.Secondary
    )
}

@Preview
@Composable
fun OutlineButtonPrimaryPreview() {
    Button(
        text = "Next",
        style = ButtonDefaultStyles.Outline
    )
}

@Preview
@Composable
fun TransparentButtonPrimaryPreview() {
    Button(
        text = "Next",
        style = ButtonDefaultStyles.Transparent
    )
}
