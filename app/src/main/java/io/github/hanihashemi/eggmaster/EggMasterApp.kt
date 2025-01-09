package io.github.hanihashemi.eggmaster

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import io.github.hanihashemi.eggmaster.screens.boilingdone.BoilingDoneScreen
import io.github.hanihashemi.eggmaster.screens.boilingtimer.BoilingTimerScreen
import io.github.hanihashemi.eggmaster.screens.contactme.ContactUsScreen
import io.github.hanihashemi.eggmaster.screens.eggboildetails.EggBoilDetailsScreen
import io.github.hanihashemi.eggmaster.screens.splash.SplashScreen
import io.github.hanihashemi.eggmaster.screens.tutorial.TutorialScreen
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
    viewModel: MainViewModel,
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
private fun EggMasterNavHost(
    navController: NavHostController,
    viewModel: MainViewModel,
) {
    val state by viewModel.viewState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = state.startDestination,
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
            startDestination = Screen.Intro.Destination.Title.route,
            route = Screen.Intro.route,
        ) {
            composable(Screen.Intro.Destination.Title.route) {
                SplashScreen(dispatch = viewModel::dispatch)
            }
            composable(Screen.Intro.Destination.Tutorial.route) {
                TutorialScreen(
                    state = state,
                    dispatch = viewModel::dispatch
                )
            }
        }

        composable(Screen.BoilDetail.route) {
            EggBoilDetailsScreen(
                state = state,
                dispatch = viewModel::dispatch,
            )
        }

        navigation(
            startDestination = Screen.Timer.Destination.BoilTimer.route,
            route = Screen.Timer.route,
        ) {
            composable(Screen.Timer.Destination.BoilTimer.route) {
                BoilingTimerScreen(
                    state = state,
                    dispatch = viewModel::dispatch,
                )
            }
        }

        navigation(
            startDestination = Screen.Finished.Destination.BoilFinish.route,
            route = Screen.Finished.route,
        ) {
            composable(Screen.Finished.Destination.BoilFinish.route) {
                BoilingDoneScreen(dispatch = viewModel::dispatch)
            }
        }

        composable(Screen.ContactUs.route) {
            ContactUsScreen(dispatch = viewModel::dispatch)
        }
    }
}