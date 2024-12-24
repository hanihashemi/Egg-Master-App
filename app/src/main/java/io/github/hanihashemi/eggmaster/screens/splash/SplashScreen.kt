package io.github.hanihashemi.eggmaster.screens.splash

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.hanihashemi.eggmaster.MainViewModel
import io.github.hanihashemi.eggmaster.R
import io.github.hanihashemi.eggmaster.components.Button
import io.github.hanihashemi.eggmaster.components.ButtonDefaultStyles
import io.github.hanihashemi.eggmaster.extensions.isPortrait
import io.github.hanihashemi.eggmaster.extensions.isTablet
import io.github.hanihashemi.eggmaster.ui.theme.Dimens
import io.github.hanihashemi.eggmaster.ui.theme.EggMasterTheme
import io.github.hanihashemi.eggmaster.ui.theme.Shapes


@Composable
fun SplashScreen(dispatch: (MainViewModel.ViewAction) -> Unit) {
    val context = LocalContext.current

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .widthIn(max = 350.dp)
                        .padding(bottom = Dimens.PaddingNormal)
                        .clip(Shapes.large)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(Dimens.PaddingNormal)
                    ) {
                        Button(
                            text = stringResource(R.string.splash_screen_button_start),
                            style = ButtonDefaultStyles.Secondary
                        ) { dispatch.invoke(MainViewModel.ViewAction.NavigateToEggDetails) }
                        Button(
                            text = stringResource(R.string.splash_screen_button_tutorial),
                            style = ButtonDefaultStyles.Outline,
                        ) { dispatch.invoke(MainViewModel.ViewAction.NavigateToTutorial) }
                        Button(
                            text = stringResource(R.string.splash_screen_button_contact_me),
                            style = ButtonDefaultStyles.Transparent,
                        ) { openEmailIntent(context) }
                    }
                }
            }
        },
    ) { paddingValues ->
        if (context.isPortrait() or context.isTablet()) {
            PortraitContent(paddingValues)
        } else {
            LandscapeContent(paddingValues)
        }
    }
}

@Composable
private fun PortraitContent(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(paddingValues)
            .padding(Dimens.PaddingNormal),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Title(modifier = Modifier.weight(1.5F))
        Box(
            modifier = Modifier
                .weight(2F)
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            EggLogoComponent()
        }
    }
}

@Composable
private fun LandscapeContent(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(paddingValues)
            .padding(Dimens.PaddingNormal),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Title(modifier = Modifier, displaySubtitle = false)
    }
}

@Composable
private fun Title(modifier: Modifier, displaySubtitle: Boolean = true) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.splash_screen_label_title),
                lineHeight = 45.sp,
                style = MaterialTheme.typography.displayMedium
            )
            if (displaySubtitle) {
                Text(
                    text = stringResource(R.string.splash_screen_label_subtitle),
                    style = MaterialTheme.typography.bodyLarge.merge(
                        TextStyle(
                            color = Color.White.copy(alpha = 0.4F)
                        )
                    )
                )
            }
        }
    }
}

private fun openEmailIntent(context: android.content.Context) {
    val email = context.getString(R.string.contact_me_email)
    val subject = context.getString(R.string.contact_me_subject)
    val body = context.getString(R.string.contact_me_body)
    val noEmailApp = context.getString(R.string.contact_me_no_email_app)

    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(
            Intent.EXTRA_EMAIL,
            arrayOf(email)
        )
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
    }

    try {
        context.startActivity(Intent.createChooser(emailIntent, subject))
    } catch (ex: android.content.ActivityNotFoundException) {
        Toast.makeText(
            context,
            noEmailApp,
            Toast.LENGTH_SHORT
        ).show()
    }
}

@Composable
private fun SplashScreenPreview() {
    EggMasterTheme {
        SplashScreen {}
    }
}

@Preview(device = "id:pixel_9_pro")
@Composable
fun Pixel9Preview() = SplashScreenPreview()

@Preview(device = "id:medium_phone")
@Composable
fun MediumPreview() = SplashScreenPreview()

@Preview(device = "id:small_phone")
@Composable
fun SmallPreview() = SplashScreenPreview()