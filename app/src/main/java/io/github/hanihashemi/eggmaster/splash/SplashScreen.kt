package io.github.hanihashemi.eggmaster.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.hanihashemi.eggmaster.components.Button
import io.github.hanihashemi.eggmaster.components.ButtonDefaultStyles
import io.github.hanihashemi.eggmaster.ui.theme.Dimens
import io.github.hanihashemi.eggmaster.ui.theme.EggMasterTheme
import io.github.hanihashemi.eggmaster.ui.theme.Shapes


@Composable
fun SplashScreen(onStartClick: () -> Unit) {
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomBar { onStartClick() } },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(paddingValues)
                .padding(Dimens.PaddingNormal),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                EggLogoComponent()
            }
        }
    }
}

@Composable
private fun TopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Dimens.PaddingXLarge),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Egg\n master",
                lineHeight = 45.sp,
                style = MaterialTheme.typography.displayMedium
            )
            Text(
                text = "prepare eggs as you like!",
                style = MaterialTheme.typography.bodyLarge.merge(
                    TextStyle(
                        color = Color.White.copy(alpha = 0.4F)
                    )
                )
            )
        }
    }
}

@Composable
private fun BottomBar(onStartClick: () -> Unit) {
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
                text = "Let's start",
                onClick = { onStartClick() },
                style = ButtonDefaultStyles.Secondary,
            )
        }
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    EggMasterTheme {
        SplashScreen(onStartClick = {})
    }
}