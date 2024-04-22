package io.github.hanihashemi.eggmaster.splash

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.hanihashemi.eggmaster.ui.theme.EggMasterTheme
import io.github.hanihashemi.eggmaster.ui.theme.YolkColor
import io.github.hanihashemi.eggmaster.ui.theme.YolkLeftEyeColor
import io.github.hanihashemi.eggmaster.ui.theme.YolkMouthColor
import kotlinx.coroutines.delay

@Composable
fun EggLogoComponent() {
    val interactionSource = remember { MutableInteractionSource() }
    var isPressed by remember { mutableStateOf(false) }
    var currentState by remember { mutableStateOf(EggLogoState.Released) }
    val transition = updateTransition(currentState, label = "Logo Transition")

    val scale by transition.animateFloat(
        label = "Logo Scale",
        transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow,
            )
        }
    ) { state ->
        when (state) {
            EggLogoState.Pressed -> 0.9F
            EggLogoState.Released -> 1F
        }
    }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    isPressed = true
                    currentState = EggLogoState.Pressed
                }
                is PressInteraction.Release -> {
                    isPressed = false
                    currentState = EggLogoState.Released
                }
                is PressInteraction.Cancel -> {
                    isPressed = false
                    currentState = EggLogoState.Released
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .aspectRatio(1F)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = createEggWhiteVectorPainter(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize(fraction = 0.7F)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                ) {}
        )

        YolkComponent(modifier = Modifier.fillMaxSize(fraction = 0.3F), reRunAnimation = isPressed)
    }
}

@Composable
fun YolkComponent(modifier: Modifier, reRunAnimation: Boolean) {
    var currentState by remember { mutableStateOf(YolkState.SmileAndWinking) }
    val transition = updateTransition(currentState, label = "Yolk Transition")

    val rightEyeClosedSweepAngle by transition.animateFloat(
        label = "Right Eye Winking",
        transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow,
            )
        }) { state ->
        when (state) {
            YolkState.SmileAndWinking -> 1F
            else -> 40F
        }
    }
    val rightEyeStrokeWidth by transition.animateFloat(
        label = "Right Eye Stroke Width",
        transitionSpec = { tween(durationMillis = 200) }) { state ->
        when (state) {
            YolkState.SmileAndWinking -> 0.058F
            else -> 0.04F
        }
    }
    val rightEyeColor by transition.animateColor(
        label = "Right Eye Color",
        transitionSpec = { tween(durationMillis = 200) }) { state ->
        when (state) {
            YolkState.SmileAndWinking -> YolkLeftEyeColor
            else -> Color.White
        }
    }
    val faceRotate by transition.animateFloat(
        label = "Face Rotation",
        transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow,
            )
        }) { state ->
        when (state) {
            YolkState.FaceRotate -> -400F
            else -> 0F
        }
    }

    LaunchedEffect(reRunAnimation) {
        if (reRunAnimation) {
            currentState = YolkState.SmileAndWinking
        } else {
            currentState = YolkState.SmileAndWinking
            delay(300)
            currentState = YolkState.FaceRotate
        }
    }

    Canvas(modifier = modifier.aspectRatio(1F)) {
        drawCircle(
            color = YolkColor,
            center = center,
            radius = size.width / 2,
        )

        rotate(degrees = faceRotate) {
            val mouthSize = size * 0.7F
            val mouthStrokeWidth = Dp(size.width * 0.04F)
            drawArc(
                color = YolkMouthColor,
                startAngle = 10F,
                sweepAngle = 160F,
                useCenter = false,
                topLeft = center - Offset(
                    mouthSize.width / 2,
                    mouthSize.height / 2
                ),
                size = mouthSize,
                style = Stroke(
                    width = mouthStrokeWidth.toPx(),
                    cap = StrokeCap.Round,
                ),
            )

            drawCircle(
                color = YolkLeftEyeColor,
                center = center - Offset(
                    size.width * 0.15F,
                    size.height * 0.3F
                ),
                radius = size.width * 0.08F,
            )

            val rightEyeClosedSize = size * 0.65F
            drawArc(
                color = rightEyeColor,
                startAngle = 276F,
                sweepAngle = rightEyeClosedSweepAngle,
                topLeft = center - Offset(
                    rightEyeClosedSize.width / 2, rightEyeClosedSize.height / 2
                ),
                size = rightEyeClosedSize,
                useCenter = false,
                style = Stroke(
                    width = Dp(size.width * rightEyeStrokeWidth).toPx(), cap = StrokeCap.Round
                )
            )
        }
    }
}

@Composable
private fun createEggWhiteVectorPainter(): Painter {
    return rememberVectorPainter(
        defaultWidth = 378.42.dp,
        defaultHeight = 376.89.dp,
        viewportWidth = 378.42F,
        viewportHeight = 376.89F,
        autoMirror = false,
    ) { _, _ ->
        val pathData = PathParser()
            .parsePathString("m34.93,120.92c1.66,20.59-13.36,38.86-20.04,58.29-6.55,19.06-13.16,38.38-14.62,58.48-1.46,20.1,2.79,41.4,15.82,56.78,14.06,16.58,35.09,25.96,55.91,32.2,37.95,11.39,68.6,30.92,103.95,43.86,28.04,10.27,78.19,9.61,108.1-13.26,16.36-12.52,30.19-29.07,38.42-47.95,12.27-28.17,29.26-39.03,42.89-61.04,8.7-14.06,12.68-32,13.05-48.53.43-19.35-7.94-38.69-14.45-56.92-7.28-20.4-8.52-42.37-12.08-63.74s-13.06-37.36-28.38-52.67c-24.35-24.32-60.7-31.01-94.29-23.49-33.58,7.52-60.25,5.64-94.66,5.41-11.19-.07-28.9,2.24-42.54,9.79-15.95,8.84-31.58,19.13-42.45,33.77s-17.4,34.59-14.63,69.03Z")
            .toNodes()

        Path(
            pathData = pathData,
            fill = SolidColor(Color.White)
        )
    }
}

private enum class YolkState {
    SmileAndWinking,
    FaceRotate,
}

private enum class EggLogoState {
    Pressed,
    Released,
}

@Preview(showBackground = true, backgroundColor = 0xFF673AB7)
@Composable
private fun EggLogoComponentPreview() {
    EggMasterTheme {
        EggLogoComponent()
    }
}