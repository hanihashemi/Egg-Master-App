package io.github.hanihashemi.eggmaster.screens.tutorial.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.hanihashemi.eggmaster.R
import io.github.hanihashemi.eggmaster.components.Button
import io.github.hanihashemi.eggmaster.ui.theme.Dimens
import io.github.hanihashemi.eggmaster.ui.theme.Shapes

private val infoText = listOf(
    R.string.intro_info_text_1,
    R.string.intro_info_text_2,
    R.string.intro_info_text_3
)
private val infoButtonText = listOf(
    R.string.intro_info_button_1,
    R.string.intro_info_button_2,
    R.string.intro_info_button_3
)

@Composable
fun InformationCards(tutorialCurrentStep: Int, onNextPressed: () -> Unit) {
    val screenWith = LocalConfiguration.current.screenWidthDp.dp

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        for (index in 0 until 3) {
            val offset by animateDpAsState(
                targetValue = when {
                    index < tutorialCurrentStep -> -screenWith
                    index > tutorialCurrentStep -> screenWith
                    else -> 0.dp
                },
                animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing),
                label = "offset animation"
            )
            val alpha by animateFloatAsState(
                targetValue = if (index == tutorialCurrentStep) 1f else 0f,
                animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing),
                label = "alpha animation",
            )
            val scale by animateFloatAsState(
                targetValue = if (index == tutorialCurrentStep) 1f else 0.8f,
                animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing),
                label = "scale animation",
            )
            Box(
                modifier = Modifier
                    .offset(x = offset)
                    .alpha(alpha)
                    .scale(scale)
                    .widthIn(max = 350.dp)
                    .padding(Dimens.PaddingNormal)
                    .clip(Shapes.large)
                    .fillMaxWidth()
                    .background(color = Color.White)
                    .padding(Dimens.PaddingLarge),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = LocalContext.current.getString(infoText[index]),
                        textAlign = TextAlign.Center,
                        color = Color(0xFF202143),
                    )

                    Spacer(modifier = Modifier.padding(Dimens.PaddingNormal))

                    Button(text = LocalContext.current.getString(infoButtonText[index])) {
                        onNextPressed()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun InformationCardsPreview() {
    MaterialTheme {
        InformationCards(0) {}
    }
}