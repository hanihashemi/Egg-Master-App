package io.github.hanihashemi.eggmaster.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import io.github.hanihashemi.eggmaster.components.BottomBarButton
import io.github.hanihashemi.eggmaster.ui.theme.Dimens
import io.github.hanihashemi.eggmaster.ui.theme.EggMasterTheme


@Composable
fun SplashScreen(onStartClick: () -> Unit) {
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomBarButton(text = "Let's start") { onStartClick() } },
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

@Preview
@Composable
fun SplashScreenPreview() {
    EggMasterTheme {
        SplashScreen(onStartClick = {})
    }
}