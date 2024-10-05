package io.github.hanihashemi.eggmaster.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.hanihashemi.eggmaster.ui.theme.Dimens
import io.github.hanihashemi.eggmaster.ui.theme.Shapes

@Composable
fun BottomBarButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 350.dp)
                .padding(Dimens.PaddingNormal)
                .clip(Shapes.large)
                .fillMaxWidth()
                .padding(Dimens.PaddingLarge),
            contentAlignment = Alignment.Center,
        ) {
            Button(
                text = text,
                onClick = { onClick() },
                style = ButtonDefaultStyles.Secondary,
            )
        }
    }
}
