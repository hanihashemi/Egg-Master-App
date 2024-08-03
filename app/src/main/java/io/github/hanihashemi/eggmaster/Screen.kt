package io.github.hanihashemi.eggmaster

import androidx.navigation.NamedNavArgument

sealed class Screen(val route: String, val navArguments: List<NamedNavArgument> = emptyList()) {

    data object Intro : Screen("intro") {
        sealed class Destination(val route: String) {
            data object Splash : Destination("splash")
            data object Tutorial : Destination("tutorial")

        }
    }

    data object Main : Screen("main") {
        sealed class Destination(val route: String) {
            data object BoilDetail : Destination("boilDetail")
            data object BoilTimer : Destination("boilTimer")
            data object BoilFinish : Destination("boilFinish")
        }
    }
}