package io.github.hanihashemi.eggmaster.screens.boilingtimer

import android.app.AlertDialog
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.hanihashemi.eggmaster.MainViewModel
import io.github.hanihashemi.eggmaster.R
import io.github.hanihashemi.eggmaster.components.BoilingPot
import io.github.hanihashemi.eggmaster.components.BottomBarButton
import io.github.hanihashemi.eggmaster.components.TopBar
import io.github.hanihashemi.eggmaster.extensions.formatSecondsToMinutes
import io.github.hanihashemi.eggmaster.ui.models.EggDetailsUiModel
import io.github.hanihashemi.eggmaster.ui.models.EggTimerUiModel
import io.github.hanihashemi.eggmaster.ui.models.UiState
import io.github.hanihashemi.eggmaster.ui.theme.ClockPointerColor
import io.github.hanihashemi.eggmaster.ui.theme.Dimens
import io.github.hanihashemi.eggmaster.ui.theme.EggMasterTheme

@Composable
fun BoilingTimerScreen(state: UiState, dispatch: (MainViewModel.ViewAction) -> Unit) {
    val context = LocalContext.current

    Scaffold(
        topBar = { TopBar(titleString = stringResource(R.string.egg_boil_timer_screen_title)) },
        bottomBar = {
            BottomBarButton(stringResource(R.string.egg_boil_timer_screen_button_cancel)) {
                val title = context.getString(R.string.egg_boil_timer_screen_dialog_title)
                val message = context.getString(R.string.egg_boil_timer_screen_dialog_message)
                val positiveButton =
                    context.getString(R.string.egg_boil_timer_screen_dialog_button_positive)
                val negativeButton =
                    context.getString(R.string.egg_boil_timer_screen_dialog_button_negative)

                AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(positiveButton) { dialog, _ ->
                        dialog.dismiss()
                        dispatch(MainViewModel.ViewAction.CancelTimer)
                    }
                    .setNegativeButton(negativeButton) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) { paddingValues ->

        Column(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            BoilingPot(dropEgg = true)
            Text(
                text = getMinutes(state.eggTimer.time),
                modifier = Modifier
                    .padding(
                        top = Dimens.PaddingLarge,
                        bottom = Dimens.PaddingXXLarge,
                    ),
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = Color.White,
                )
            )
            ClockFace(state.eggTimer.time)
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.PaddingLarge)
                    .height(1.dp)
                    .background(Color.White.copy(alpha = 0.2f))
            )
            Text(
                text = stringResource(R.string.egg_boil_timer_screen_subtitle),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = stringResource(R.string.egg_boil_timer_screen_label_description),
                modifier = Modifier
                    .padding(top = Dimens.PaddingSmall)
                    .padding(horizontal = Dimens.PaddingNormal),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            )
        }
    }
}

private fun getMinutes(time: Int): String = time.takeIf { it != 0 }?.formatSecondsToMinutes() ?: ""

@Composable
private fun ClockFace(time: Int) {
    var rotationAngel by remember { mutableFloatStateOf(0f) }
    val animatedRotationAngel by animateFloatAsState(
        targetValue = rotationAngel,
        label = "Clock face rotation"
    )

    LaunchedEffect(time) {
        rotationAngel += 6f
    }

    CustomClockLayout(
        modifier = Modifier
            .fillMaxWidth()
            .scale(2.5f)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_clock_face),
                contentDescription = "Clock face",
                modifier = Modifier
                    .fillMaxWidth()
                    .rotate(animatedRotationAngel)
            )
            Canvas(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(20.dp)
            ) {
                drawCircle(
                    color = ClockPointerColor,
                    radius = 2f.dp.toPx(),
                )
            }
        }
    }
}

@Composable
fun CustomClockLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Layout(
        content = content,
        modifier = modifier,
    ) { measures, constraints ->
        val placeable = measures.map { measurable ->
            measurable.measure(constraints)
        }
        val layoutWidth = constraints.maxWidth
        val layoutHeight = (constraints.maxWidth / 9f).toInt()
        layout(layoutWidth, layoutHeight) {
            placeable.forEach { placeable ->
                placeable.placeRelative(
                    x = 0,
                    y = ((placeable.height / 1.1) * -1f).toInt()
                )
            }
        }
    }
}


@Preview(device = "id:pixel_4")
@Composable
private fun BoilingTimerScreenSmallSizePreview() {
    EggMasterTheme {
        BoilingTimerScreen(
            state = UiState(
                eggDetails = EggDetailsUiModel(),
                startDestination = "",
                eggTimer = EggTimerUiModel(),
            ),
            dispatch = { },
        )
    }
}

@Preview(device = "id:pixel_7a")
@Composable
private fun BoilingTimerScreenMediumSizePreview() {
    EggMasterTheme {
        BoilingTimerScreen(
            state = UiState(
                eggDetails = EggDetailsUiModel(),
                startDestination = "",
                eggTimer = EggTimerUiModel(),
            ),
            dispatch = { },
        )
    }
}

@Preview(device = "id:pixel_8_pro")
@Composable
private fun BoilingTimerScreenLargeSizePreview() {
    EggMasterTheme {
        BoilingTimerScreen(
            state = UiState(
                eggDetails = EggDetailsUiModel(),
                startDestination = "",
                eggTimer = EggTimerUiModel(),
            ),
            dispatch = { },
        )
    }
}