package io.github.hanihashemi.eggmaster.splashscreen

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun EggLogoComponent() {
    Box(modifier = Modifier.aspectRatio(1F), contentAlignment = Alignment.Center) {
        Image(
            painter = createEggWhiteVectorPainter(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(fraction = 0.7F)
        )

        YolkComponent(modifier = Modifier.fillMaxSize(fraction = 0.3F))
    }
}

@Composable
private fun YolkComponent(modifier: Modifier) {
    var rightEyeToggle by remember { mutableStateOf(false) }
    val rightEyeClosedSweepAngle: Float by animateFloatAsState(
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow,
        ),
        targetValue = if (rightEyeToggle) 40F else 1F
    )
    val rightEyeStrokeWidth: Float by animateFloatAsState(
        animationSpec = tween(
            durationMillis = 200
        ),
        targetValue = if (rightEyeToggle) 0.04F else 0.058F
    )
    var faceRotationToggle by remember { mutableStateOf(false) }
    val faceRotate: Float by animateFloatAsState(
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessVeryLow,
        ),
        targetValue = if (faceRotationToggle) -400F else 0F
    )

    LaunchedEffect(true) {
        delay(50)
        rightEyeToggle = true
        delay(300)
        faceRotationToggle = true
    }

    Canvas(
        modifier = modifier
            .aspectRatio(1F)
    ) {
        drawCircle(
            color = Color(0XFFFFBA4B), center = center, radius = size.width / 2
        )

        rotate(faceRotate) {

            val mouthSize = size * 0.7F
            val mouthStrokeWidth = Dp(size.width * 0.04F)
            drawArc(
                color = Color(0XFFFAA847),
                startAngle = 10F,
                sweepAngle = 160F,
                topLeft = center - Offset(
                    mouthSize.width / 2, mouthSize.height / 2
                ),
                size = mouthSize,
                useCenter = false,
                style = Stroke(
                    width = mouthStrokeWidth.toPx(), cap = StrokeCap.Round
                )
            )

            drawCircle(
                color = Color(0XFFFFE0B3), center = center - Offset(
                    size.width * 0.15F, size.height * 0.3F
                ), radius = size.width * 0.08F
            )

            val rightEyeClosedSize = size * 0.65F
            drawArc(
                color = Color(0XFFFFFFFF),
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
        autoMirror = false
    ) { _, _ ->
        val pathData = PathParser()
            .parsePathString("m34.93,120.92c1.66,20.59 -13.36,38.86 -20.04,58.29 -6.55,19.06 -13.16,38.38 -14.62,58.48 -1.46,20.1 2.79,41.4 15.82,56.78 14.06,16.58 35.09,25.96 55.91,32.2 37.95,11.39 68.6,30.92 103.95,43.86 28.04,10.27 78.19,9.61 108.1,-13.26 16.36,-12.52 30.19,-29.07 38.42,-47.95 12.27,-28.17 29.26,-39.03 42.89,-61.04 8.7,-14.06 12.68,-32 13.05,-48.53 0.43,-19.35 -7.94,-38.69 -14.45,-56.92 -7.28,-20.4 -8.52,-42.37 -12.08,-63.74s-13.06,-37.36 -28.38,-52.67c-24.35,-24.32 -60.7,-31.01 -94.29,-23.49 -33.58,7.52 -60.25,5.64 -94.66,5.41 -11.19,-0.07 -28.9,2.24 -42.54,9.79 -15.95,8.84 -31.58,19.13 -42.45,33.77s-17.4,34.59 -14.63,69.03Z")
            .toNodes()

        Path(
            pathData = pathData,
            fill = SolidColor(Color.White)
        )
    }
}