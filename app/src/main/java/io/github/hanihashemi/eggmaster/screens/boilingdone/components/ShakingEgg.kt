package io.github.hanihashemi.eggmaster.screens.boilingdone.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import io.github.hanihashemi.eggmaster.R

private const val EGG_POSITION = -40

@Composable
fun ShakingEgg() {
    var alpha by remember { mutableFloatStateOf(0f) }
    val animatedAlpha by animateFloatAsState(
        targetValue = alpha,
        label = "alpha",
        animationSpec = tween(
            durationMillis = 2000, // 2 seconds
            easing = LinearOutSlowInEasing,
        )
    )

    var position by remember { mutableStateOf(EGG_POSITION.dp) }
    val animatedPosition by animateDpAsState(
        targetValue = position,
        label = "position",
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessVeryLow,
        )
    )

    val infiniteTransition = rememberInfiniteTransition("rotation infinite")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 10f,
        targetValue = 15f,
        label = "rotation",
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 100, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        )
    )

    LaunchedEffect(Unit) {
        alpha = 1f
        position = 0.dp
    }

    Image(
        painter = painterResource(id = R.drawable.ic_egg),
        contentDescription = "Shaking egg with animation",
        modifier = Modifier
            .offset { IntOffset(x = 0, y = animatedPosition.roundToPx()) }
            .rotate(rotation)
            .alpha(animatedAlpha)

    )
}