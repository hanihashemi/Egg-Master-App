package io.github.hanihashemi.eggmaster.tutorial.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.github.hanihashemi.eggmaster.R

private const val EGG_ROTATION = 40F
private const val EGG_POSITION = -40
private const val EGG_ALPHA_ANIMATION_LABEL = "Egg Alpha Animation"
private const val EGG_POSITION_ANIMATION_LABEL = "Egg Position Animation"

@Composable
fun EggAnimated(dropEgg: Boolean) {
    var eggAlpha by remember { mutableFloatStateOf(0f) }

    val animatedEggAlpha by animateFloatAsState(
        targetValue = eggAlpha,
        animationSpec = tween(
            durationMillis = 2000,
            easing = LinearOutSlowInEasing,
        ),
        label = EGG_ALPHA_ANIMATION_LABEL,
    )


    var eggPosition by remember { mutableStateOf(EGG_POSITION.dp) }
    val animatedEggPosition by animateDpAsState(
        targetValue = eggPosition,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessVeryLow,
        ),
        label = EGG_POSITION_ANIMATION_LABEL,
    )
    LaunchedEffect(dropEgg) {
        if (dropEgg) {
            eggPosition = 10.dp
            eggAlpha = 1F
        } else {
            eggPosition = EGG_POSITION.dp
            eggAlpha = 0F
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            modifier = Modifier
                .size(40.dp)
                .offset(y = animatedEggPosition)
                .rotate(EGG_ROTATION)
                .alpha(animatedEggAlpha),
            painter = painterResource(id = R.drawable.ic_egg),
            contentDescription = null,
        )
    }
}