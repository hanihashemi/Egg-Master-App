package io.github.hanihashemi.eggmaster

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import io.github.hanihashemi.eggmaster.eggboildetails.EggBoilDetailsScreen
import io.github.hanihashemi.eggmaster.splash.SplashScreen
import io.github.hanihashemi.eggmaster.tutorial.TutorialScreen
import io.github.hanihashemi.eggmaster.ui.models.UiState

@Composable
fun EggMasterApp(
    navController: NavHostController,
    viewModel: MainViewModel,
    state: UiState,
) {
    EggMasterNavHost(navController, viewModel)
    HandleTutorialScreenBackButton(navController, state, viewModel)
}

@Composable
private fun HandleTutorialScreenBackButton(
    navController: NavHostController,
    state: UiState,
    viewModel: MainViewModel
) {
    val isCurrentScreenTutorial = navController
        .currentBackStackEntryFlow
        .collectAsState(initial = null)
        .value?.destination?.route == Screen.Intro.Destination.Tutorial.route
            && state.tutorialCurrentStep != 0
    BackHandler(isCurrentScreenTutorial) {
        viewModel.dispatch(MainViewModel.ViewAction.TutorialPreviousPressed)
    }
}

@Composable
private fun EggMasterNavHost(navController: NavHostController, viewModel: MainViewModel) {
    val state by viewModel.viewState.collectAsState()

    NavHost(navController = navController,
        startDestination = Screen.Intro.route,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(700)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(700)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(700)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(700)
            )
        }) {
        navigation(
            startDestination = Screen.Intro.Destination.Splash.route, route = Screen.Intro.route
        ) {
            composable(Screen.Intro.Destination.Splash.route) {
                SplashScreen {
                    navController.navigate(Screen.Intro.Destination.Tutorial.route)
                }
            }
            composable(Screen.Intro.Destination.Tutorial.route) {
                TutorialScreen(
                    state = state,
                    dispatch = viewModel::dispatch
                )
            }
        }

        navigation(
            startDestination = Screen.Main.Destination.BoilDetail.route, route = Screen.Main.route
        ) {
            composable(Screen.Main.Destination.BoilDetail.route) {
                EggBoilDetailsScreen(
                    state = state,
                    dispatch = viewModel::dispatch
                )
            }
            composable(Screen.Main.Destination.BoilTimer.route) {
                Column {
                    Text("Boil Timer")

                    Button(onClick = {
                        navController.navigate("boil_finish")
                    }) {
                        Text("Boil Finish")
                    }
                }
            }
            composable(Screen.Main.Destination.BoilFinish.route) {
                Column {
                    Text("Boil Finish")

                    Button(onClick = {
                        navController.navigate("main") {
                            popUpTo("main") {
                                inclusive = true
                            }
                        }
                    }) {
                        Text("Boil Details")
                    }
                }
            }
        }
    }
}