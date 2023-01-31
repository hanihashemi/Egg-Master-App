package io.github.hanihashemi.eggmaster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.hanihashemi.eggmaster.splashscreen.EggLogoComponent
import io.github.hanihashemi.eggmaster.ui.theme.EggMasterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EggMasterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Box(
                        contentAlignment = Alignment.Center, modifier = Modifier
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .weight(1F)
                                    .fillMaxSize()
                                    .padding(16.dp),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                Column {
                                    Text(
                                        text = "Egg\n  master",
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
                            Box(
                                modifier = Modifier
                                    .weight(2F)
                                    .fillMaxSize(), contentAlignment = Alignment.Center
                            ) { EggLogoComponent() }
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
                                    onClick = {  }) {
                                    Text(text = "Lets Start", color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}