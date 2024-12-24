package io.github.hanihashemi.eggmaster.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.hanihashemi.eggmaster.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    titleString: String = "",
    onMenuClick: (() -> Unit)? = null,
    onBackClicked: (() -> Unit)? = null,
    onCloseClicked: (() -> Unit)? = null,
    title: (@Composable () -> Unit)? = null,
) {
    val endPadding = when {
        onMenuClick != null || onBackClicked != null -> Dimens.PaddingXXLarge
        else -> Dimens.PaddingNormal
    }

    TopAppBar(
        modifier = Modifier.padding(end = endPadding),
        title = {
            title?.invoke() ?: Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(titleString)
            }
        },
        navigationIcon = {
            onMenuClick?.let {
                IconButton(
                    onClick = onMenuClick,
                ) {
                    Icon(Icons.Filled.Menu, contentDescription = "Open navigation drawer")
                }
            }

            onBackClicked?.let {
                IconButton(
                    onClick = onBackClicked,
                ) {
                    Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                }
            }

            onCloseClicked?.let {
                IconButton(
                    onClick = onCloseClicked,
                ) {
                    Icon(Icons.Filled.Close, contentDescription = "Close")
                }
            }
        },
    )
}

@Composable
@Preview
fun TopBarPreview() {
    TopBar("Egg Master")
}