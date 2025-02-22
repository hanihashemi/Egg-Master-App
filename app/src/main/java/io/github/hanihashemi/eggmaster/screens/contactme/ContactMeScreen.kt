package io.github.hanihashemi.eggmaster.screens.contactme

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.hanihashemi.eggmaster.MainViewModel
import io.github.hanihashemi.eggmaster.R
import io.github.hanihashemi.eggmaster.components.Button
import io.github.hanihashemi.eggmaster.components.ButtonDefaultStyles
import io.github.hanihashemi.eggmaster.components.TopBar
import io.github.hanihashemi.eggmaster.ui.theme.ContactUsLayoutBackgroundColor
import io.github.hanihashemi.eggmaster.ui.theme.Dimens
import io.github.hanihashemi.eggmaster.ui.theme.EggMasterTheme
import io.github.hanihashemi.eggmaster.ui.theme.Shapes

@Composable
fun ContactUsScreen(
    dispatch: (MainViewModel.ViewAction) -> Unit,
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopBar(
                titleString = stringResource(id = R.string.splash_screen_button_contact_me),
                onBackClicked = { dispatch(MainViewModel.ViewAction.NavigateBack) },
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 420.dp)
                    .fillMaxHeight()
                    .padding(Dimens.PaddingNormal)
                    .verticalScroll(
                        rememberScrollState(),
                    ),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {

                Column {
                    Row(
                        modifier = Modifier
                            .clip(Shapes.medium)
                            .background(ContactUsLayoutBackgroundColor)
                            .padding(Dimens.PaddingNormal),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            modifier = Modifier.padding(end = Dimens.PaddingSmall),
                        )
                        Text(text = stringResource(id = R.string.contact_us_screen_description_1))
                    }

                    Spacer(modifier = Modifier.height(Dimens.PaddingLarge))

                    Row(
                        modifier = Modifier
                            .clip(Shapes.medium)
                            .background(ContactUsLayoutBackgroundColor)
                            .padding(Dimens.PaddingNormal),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.padding(end = Dimens.PaddingSmall),
                        )
                        Text(text = stringResource(id = R.string.contact_us_screen_description_2))
                    }
                }

                Column {
                    Button(
                        text = stringResource(id = R.string.contact_us_screen_button_email_feedback),
                        onClick = { openEmailIntent(context) },
                        modifier = Modifier.padding(top = Dimens.PaddingNormal),
                        style = ButtonDefaultStyles.Secondary,
                    )
                    Button(
                        text = stringResource(id = R.string.contact_us_screen_button_join_alpha_testing),
                        onClick = { openGoogleGroup(context) },
                        modifier = Modifier.padding(top = Dimens.PaddingNormal),
                        style = ButtonDefaultStyles.Secondary,
                    )
                    Button(
                        text = stringResource(id = R.string.contact_us_screen_button_youtube),
                        onClick = { openYoutube(context) },
                        modifier = Modifier.padding(top = Dimens.PaddingNormal),
                        style = ButtonDefaultStyles.Transparent,
                    )
                }
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

private fun openGoogleGroup(context: android.content.Context) {
    val googleGroupUrl = context.getString(R.string.contact_us_join_alpha_testing_google_group_url)
    val noGoogleGroup = context.getString(R.string.contact_us_join_alpha_testing_no_chrome)

    val youtubeIntent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(googleGroupUrl)
    }

    try {
        context.startActivity(youtubeIntent)
    } catch (ex: android.content.ActivityNotFoundException) {
        Toast.makeText(
            context,
            noGoogleGroup,
            Toast.LENGTH_SHORT
        ).show()
    }
}

private fun openYoutube(context: android.content.Context) {
    val youtubeUrl = context.getString(R.string.contact_us_youtube_channel_url)
    val noYoutubeApp = context.getString(R.string.contact_us_youtube_channel_not_found)

    val youtubeIntent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(youtubeUrl)
    }

    try {
        context.startActivity(youtubeIntent)
    } catch (ex: android.content.ActivityNotFoundException) {
        Toast.makeText(
            context,
            noYoutubeApp,
            Toast.LENGTH_SHORT
        ).show()
    }
}

@Composable
private fun ContactMeScreenPreview() {
    EggMasterTheme {
        ContactUsScreen(dispatch = {})
    }
}

@Preview(device = "id:pixel_9_pro")
@Composable
fun Pixel9Preview() = ContactMeScreenPreview()

@Preview(device = "id:medium_phone")
@Composable
fun MediumPreview() = ContactMeScreenPreview()

@Preview(device = "id:small_phone")
@Composable
fun SmallPreview() = ContactMeScreenPreview()