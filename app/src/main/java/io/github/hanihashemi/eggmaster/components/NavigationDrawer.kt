package io.github.hanihashemi.eggmaster.components

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import io.github.hanihashemi.eggmaster.Screen
import io.github.hanihashemi.eggmaster.ui.theme.Dimens
import io.github.hanihashemi.eggmaster.ui.theme.EggMasterTheme
import io.github.hanihashemi.eggmaster.ui.theme.NavigationDrawerItemBackgroundColor
import kotlinx.coroutines.launch


/**
 * Navigation drawer for the app.
 *
 * INFO: This component is not used in the app for now.
 */
@Composable
fun NavigationDrawer(
    activity: ComponentActivity,
    navController: NavHostController,
    drawerState: DrawerState,
    content: @Composable () -> Unit,
) {
    EggMasterTheme {
        val coroutineScope = rememberCoroutineScope()
        val isCurrentScreenTutorial = navController
            .currentBackStackEntryFlow
            .collectAsState(initial = null)
            .value?.destination?.route == Screen.Intro.Destination.Tutorial.route
        val isCurrentScreenHome = navController
            .currentBackStackEntryFlow
            .collectAsState(initial = null)
            .value?.destination?.route == Screen.Intro.Destination.Title.route

        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = drawerState.isOpen,
            drawerContent = {
                ModalDrawerSheet {
                    Text(
                        text = "Egg Master",
                        modifier = Modifier.padding(Dimens.PaddingNormal),
                        style = MaterialTheme.typography.headlineMedium
                            .copy(color = MaterialTheme.colorScheme.primary),
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = Dimens.PaddingSmall))
                    NavigationDrawerItem(
                        label = { Text(text = "Home") },
                        icon = { Icon(Icons.Default.Home, contentDescription = null) },
                        selected = isCurrentScreenHome,
                        onClick = {
                            navController.currentDestination?.route?.let { route ->
                                if (route != Screen.Intro.Destination.Title.route) {
                                    coroutineScope.launch {
                                        drawerState.close()
                                    }

                                    navController.navigate(Screen.Intro.route) {
                                        popUpTo(Screen.Intro.route) {
                                            inclusive = true
                                        }
                                    }
                                }
                            }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = NavigationDrawerItemBackgroundColor
                        )
                    )
                    NavigationDrawerItem(
                        label = { Text(text = "Tutorial") },
                        icon = { Icon(Icons.Default.Info, contentDescription = null) },
                        selected = isCurrentScreenTutorial,
                        onClick = {
                            navController.currentDestination?.route?.let { route ->
                                if (route != Screen.Intro.Destination.Tutorial.route) {
                                    coroutineScope.launch {
                                        drawerState.close()
                                    }

                                    navController.navigate(Screen.Intro.Destination.Tutorial.route) {
                                        popUpTo(Screen.Intro.route) {
                                            inclusive = true
                                        }
                                    }
                                }
                            }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = NavigationDrawerItemBackgroundColor
                        )
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = Dimens.PaddingSmall))
                    NavigationDrawerItem(
                        label = { Text(text = "Contact me") },
                        icon = { Icon(Icons.Default.Email, contentDescription = null) },
                        selected = false,
                        onClick = {
                            coroutineScope.launch {
                                drawerState.close()
                            }
                            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:")
                                putExtra(
                                    Intent.EXTRA_EMAIL,
                                    arrayOf("jhanihashemi+eggmaster@gmail.com")
                                )
                                putExtra(Intent.EXTRA_SUBJECT, "Egg Master Feedback")
                                putExtra(Intent.EXTRA_TEXT, "Your feedback here ...")
                            }

                            try {
                                activity.startActivity(
                                    Intent.createChooser(
                                        emailIntent,
                                        "Egg Master Feedback"
                                    )
                                )
                            } catch (ex: android.content.ActivityNotFoundException) {
                                Toast.makeText(
                                    activity,
                                    "No email clients installed.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    )
                }
            },
        ) { content() }
    }
}