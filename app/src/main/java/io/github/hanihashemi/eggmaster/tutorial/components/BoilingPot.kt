package io.github.hanihashemi.eggmaster.tutorial.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import io.github.hanihashemi.eggmaster.R
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

private const val BUBBLE_MIN_RADIUS = 8f
private const val BUBBLE_MAX_RADIUS = 12
private const val BUBBLE_SIZE = 10
private const val BUBBLE_ALPHA_ANIMATION_LABEL = "Bubble Alpha Animation"
private const val BUBBLE_POSITION_ANIMATION_LABEL = "Bubble Position Animation"
private const val BUBBLE_ANIMATION_DURATION_FROM = 500
private const val BUBBLE_ANIMATION_DURATION_TO = 600
private const val BUBBLE_ANIMATION_DELAY_FROM = 450
private const val BUBBLE_ANIMATION_DELAY_TO = 600
private const val STEAM_ANIMATION_DURATION = 1200

@Composable
fun BoilingPot(dropEgg: Boolean) {
    Box(
        modifier = Modifier
            .aspectRatio(1F)
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {

        var center by remember { mutableStateOf(Offset.Zero) }

        Box(modifier = Modifier
            .fillMaxSize(0.5F)
            .onGloballyPositioned { coordinates ->
                val boxCenter = Offset(
                    x = coordinates.size.width / 2f,
                    y = coordinates.size.height / 2f
                )
                center = boxCenter
            }
        ) {

            Steam(
                modifier = Modifier
                    .offset(8.dp, 10.dp)
                    .scale(2F)
            )
            Steam(
                modifier = Modifier
                    .offset(18.dp, 20.dp)
                    .scale(1.5F)
            )
        }

        Image(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(1f)
                .padding(24.dp),
            painter = painterResource(id = R.drawable.ic_boiling_pot),
            contentDescription = "Boiling Pot"
        )

        Bubbles(
            Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.3F)
                .fillMaxHeight(0.17F)
        )
        EggAnimated(dropEgg)
    }

    Image(
        painter = painterResource(id = R.drawable.ic_boiling_pot_background),
        contentDescription = null
    )
}

@Composable
private fun Bubbles(modifier: Modifier) {
    var boxSize by remember { mutableStateOf(IntSize.Zero) }

    Box(modifier = modifier
        .onGloballyPositioned { coordinates ->
            boxSize = coordinates.size
        }
    ) {
        if (boxSize != IntSize.Zero) {
            val bubbles = remember {
                mutableListOf<Bubble>().apply {
                    while (size < BUBBLE_SIZE) {
                        val random = Random
                        val x = random.nextFloat() * boxSize.width
                        val y = random.nextFloat() * boxSize.height
                        val radius = (random.nextFloat() * BUBBLE_MAX_RADIUS)
                            .coerceAtLeast(BUBBLE_MIN_RADIUS)
                        val newBubble = Bubble(Offset(x, y), radius)
                        if (!doesIntersect(newBubble, this)) {
                            add(newBubble)
                        }
                    }
                }
            }

            bubbles.forEach { bubble ->
                BubbleAnimated(bubble)
            }
        }
    }
}

@Composable
private fun BubbleAnimated(bubble: Bubble) {
    val duration =
        Random.nextInt(from = BUBBLE_ANIMATION_DURATION_FROM, until = BUBBLE_ANIMATION_DURATION_TO)
    val delayMillis =
        Random.nextInt(from = BUBBLE_ANIMATION_DELAY_FROM, until = BUBBLE_ANIMATION_DELAY_TO)

    val alphaInfiniteTransition = rememberInfiniteTransition(label = BUBBLE_ALPHA_ANIMATION_LABEL)
    val alpha by alphaInfiniteTransition.animateFloat(
        targetValue = 1f,
        initialValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = duration,
                easing = FastOutSlowInEasing,
                delayMillis = delayMillis,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = BUBBLE_ALPHA_ANIMATION_LABEL,
    )

    val infiniteTransition = rememberInfiniteTransition(label = BUBBLE_POSITION_ANIMATION_LABEL)
    val centerY by infiniteTransition.animateFloat(
        initialValue = bubble.center.y,
        targetValue = bubble.center.y - 30,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = duration,
                easing = FastOutSlowInEasing,
                delayMillis = delayMillis,
            ),
            repeatMode = RepeatMode.Restart,
        ), label = BUBBLE_POSITION_ANIMATION_LABEL
    )

    val radius = bubble.radius * alpha

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            color = Color.White.copy(alpha = alpha),
            center = Offset(bubble.center.x, centerY),
            radius = radius,
            style = Stroke(width = 4f)
        )
    }
}

@Composable
private fun Steam(modifier: Modifier = Modifier) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(key1 = progress) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = STEAM_ANIMATION_DURATION,
                    easing = LinearOutSlowInEasing,
                ),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    Canvas(modifier.size(50.dp)) {
        val path = Path()
        path.moveTo(size.width / 2f, size.height)
        path.cubicTo(
            size.width / 2f - 10 * progress.value,
            size.height - 30 * progress.value,
            size.width / 2f + 10 * progress.value,
            size.height - 30 * progress.value,
            size.width / 2f,
            size.height - 50 * progress.value
        )
        drawPath(path, Color.White, style = Stroke(width = 5f))
    }
}

private fun doesIntersect(newBubble: Bubble, existingBubbles: List<Bubble>): Boolean {
    existingBubbles.forEach { existingBubble ->
        val distance = sqrt(
            (newBubble.center.x - existingBubble.center.x).pow(2) +
                    (newBubble.center.y - existingBubble.center.y).pow(2)
        )
        if (distance < newBubble.radius + existingBubble.radius) {
            return true
        }
    }
    return false
}

data class Bubble(val center: Offset, val radius: Float, val alpha: Float = 0f)
