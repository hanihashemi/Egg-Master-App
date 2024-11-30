package io.github.hanihashemi.eggmaster.components

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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import io.github.hanihashemi.eggmaster.R
import io.github.hanihashemi.eggmaster.screens.tutorial.components.EggDropIntoPot
import io.github.hanihashemi.eggmaster.ui.theme.EggMasterTheme
import io.github.hanihashemi.eggmaster.ui.theme.Shapes
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
fun BoilingPot(modifier: Modifier = Modifier, dropEgg: Boolean) {
    Box(
        modifier = Modifier
            .size(width = 300.dp, height = 180.dp)
            .then(modifier),
        contentAlignment = Alignment.Center,
    ) {
        SteamBox(
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.TopStart)
                .offset(x = 70.dp, y = (-10).dp)
        )
        Image(
            modifier = Modifier
                .align(Alignment.Center)
                .width(300.dp),
            painter = painterResource(id = R.drawable.ic_boiling_pot),
            contentDescription = "Boiling Pot"
        )
        Bubbles(
            Modifier
                .size(width = 110.dp, height = 60.dp)
                .offset(y = 15.dp)
        )
        EggDropIntoPot(dropEgg)
    }
}

@Composable
private fun SteamBox(modifier: Modifier) {
    Box(modifier = modifier) {
        Steam(
            modifier = Modifier
                .offset(0.dp, 0.dp)
                .scale(2F)
        )
        Steam(
            modifier = Modifier
                .offset(8.dp, 10.dp)
                .scale(1.5F)
        )
    }
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

    Canvas(modifier = Modifier) {
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


@Preview
@Composable
private fun BoilingPotPreview() {
    EggMasterTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF5E5E5E)),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .clip(shape = Shapes.medium)
                    .background(Color(0xFF252F72)),
                contentAlignment = Alignment.Center,
            ) {
                BoilingPot(
                    modifier = Modifier.background(Color.Red),
                    dropEgg = true,
                )
            }
        }
    }
}