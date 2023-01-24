package io.github.hanihashemi.eggmaster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
                                    .padding(16.dp)
                                    .background(Color.Red),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                Column {
                                    Text(text = "Egg")
                                    Text(text = "master")
                                    Text(text = "prepare eggs as you like!")
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .weight(2F)
                                    .fillMaxSize()
                                    .background(Color.Green), contentAlignment = Alignment.Center
                            ) {
                                EggLogoComponent()
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1F)
                                    .fillMaxSize()
                                    .background(Color.Blue),
                                contentAlignment = Alignment.Center
                            ) {
                                Button(onClick = { /*TODO*/ }) {
                                    Text(text = "Button")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}