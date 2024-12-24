package io.github.hanihashemi.eggmaster.screens.eggboildetails.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.hanihashemi.eggmaster.R
import io.github.hanihashemi.eggmaster.extensions.formatSecondsToMinutes
import io.github.hanihashemi.eggmaster.ui.models.EggDetailsUiModel
import io.github.hanihashemi.eggmaster.ui.models.EggTimerUiModel
import io.github.hanihashemi.eggmaster.ui.models.UiState
import io.github.hanihashemi.eggmaster.ui.theme.Dimens
import io.github.hanihashemi.eggmaster.ui.theme.EggMasterTheme
import io.github.hanihashemi.eggmaster.ui.theme.Shapes

@Composable
fun BoilingTimeBottomBar(state: UiState, onClick: () -> Unit = {}) {

    val animatedSeconds by animateIntAsState(
        targetValue = state.eggDetails.boilingTime,
        animationSpec = tween(
            durationMillis = 2000,
            easing = FastOutSlowInEasing,
        ),
        label = "Time animation",
    )

    val timeText = buildAnnotatedString {
        append(animatedSeconds.formatSecondsToMinutes())
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
            )
        ) {
            append(" MIN")
        }
    }

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Row(
            modifier = Modifier
                .widthIn(max = 350.dp)
                .padding(top = Dimens.PaddingNormal)
                .padding(horizontal = Dimens.PaddingNormal)
                .clip(Shapes.large)
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(Dimens.PaddingLarge),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(
                    text = stringResource(R.string.egg_boil_details_screen_bottom_bar_title),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = timeText,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                    )
                )
            }

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable { onClick() },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = stringResource(R.string.egg_boil_details_screen_bottom_bar_button_content_description_next),
                    tint = Color.White,
                )
            }
        }
    }
}

@Composable
@Preview
fun BottomBarButtonPreview() {
    EggMasterTheme {
        BoilingTimeBottomBar(
            state = UiState(
                eggDetails = EggDetailsUiModel(),
                startDestination = "",
                eggTimer = EggTimerUiModel(),
            ),
            onClick = {},
        )
    }
}
