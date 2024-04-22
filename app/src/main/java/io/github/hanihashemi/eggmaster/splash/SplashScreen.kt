package io.github.hanihashemi.eggmaster.splash

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun SplashScreen(onStartClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .weight(1F)
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Egg\n master",
                        lineHeight = 45.sp,
                        style = MaterialTheme.typography.displayMedium
                    )
                    Text(
                        "prepare eggs as you like!",
                        style = MaterialTheme.typography.bodyLarge.merge(
                            TextStyle(
                                color = Color.White.copy(alpha = 0.4F)
                            )
                        )
                    )
                }

            }
            Box(
                modifier = Modifier
                    .weight(2F)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                EggLogoComponent()
            }
            Box(
                modifier = Modifier
                    .weight(1F)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                OutlinedButton(
                    modifier = Modifier
                        .widthIn(max = 250.dp)
                        .fillMaxWidth(),
                    border = BorderStroke(2.dp, Color.White),
                    onClick = onStartClick
                ) {
                    Text(text = "Let's start", color = Color.White)
                }
            }

        }
    }
}