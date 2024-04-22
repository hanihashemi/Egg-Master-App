package io.github.hanihashemi.eggmaster.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import io.github.hanihashemi.eggmaster.ui.theme.Dimens
import io.github.hanihashemi.eggmaster.ui.theme.Shapes
import io.github.hanihashemi.eggmaster.ui.theme.Typography

@Composable
fun Button(
    text: String,
    style: ButtonStyle = ButtonDefaultStyles.Primary,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(Shapes.large)
            .background(color = style.backgroundColor)
            .clickable { onClick() }
            .padding(Dimens.PaddingNormal),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = style.textColor,
            style = Typography.displaySmall, // TODO: use Theme
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

}

data class ButtonStyle(
    val backgroundColor: Color,
    val textColor: Color,
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
