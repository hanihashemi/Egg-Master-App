package io.github.hanihashemi.eggmaster.screens.boilingdone

import android.app.NotificationManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getSystemService
import io.github.hanihashemi.eggmaster.MainViewModel
import io.github.hanihashemi.eggmaster.R
import io.github.hanihashemi.eggmaster.components.BottomBarButton
import io.github.hanihashemi.eggmaster.screens.boilingdone.components.ShakingEgg
import io.github.hanihashemi.eggmaster.ui.theme.Dimens
import io.github.hanihashemi.eggmaster.ui.theme.EggMasterTheme
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Angle
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.Spread
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

@Composable
fun BoilingDoneScreen(dispatch: (MainViewModel.ViewAction) -> Unit) {
    val context = LocalContext.current
    val notificationManager = getSystemService(context, NotificationManager::class.java)

    LaunchedEffect(Unit) {
        notificationManager?.cancelAll()
        dispatch(MainViewModel.ViewAction.OnDoneScreenShown)
    }

    Scaffold(
        bottomBar = {
            BottomBarButton(stringResource(R.string.egg_boil_result_screen_button_done)) {
                dispatch(MainViewModel.ViewAction.OnDonePressed)
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                ShakingEgg()
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Dimens.PaddingXLarge)
                        .padding(Dimens.PaddingLarge)
                        .height(1.dp)
                        .background(Color.White.copy(alpha = 0.2f))
                )

                Text(
                    text = stringResource(R.string.egg_boil_result_screen_title),
                    style = MaterialTheme.typography.bodyLarge,
                )

                Text(
                    text = stringResource(R.string.egg_boil_result_screen_description),
                    modifier = Modifier
                        .padding(top = Dimens.PaddingSmall)
                        .padding(horizontal = Dimens.PaddingLarge),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    ),
                )
            }

            KonfettiView(
                modifier = Modifier.fillMaxSize(),
                parties = parade(),
            )
        }
    }
}

fun parade(): List<Party> {
    val party = Party(
        speed = 10f,
        maxSpeed = 30f,
        damping = 0.9f,
        angle = Angle.RIGHT - 45,
        spread = Spread.SMALL,
        colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
        emitter = Emitter(duration = 10, TimeUnit.SECONDS).perSecond(30),
        position = Position.Relative(0.0, 0.4)
    )

    return listOf(
        party,
        party.copy(
            angle = party.angle - 90, // flip angle from right to left
            position = Position.Relative(1.0, 0.4)
        ),
    )
}

@Preview
@Composable
fun BoilingDoneScreenPreview() {
    EggMasterTheme {
        BoilingDoneScreen(
            dispatch = {},
        )
    }
}