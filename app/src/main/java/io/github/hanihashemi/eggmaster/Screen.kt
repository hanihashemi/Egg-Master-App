package io.github.hanihashemi.eggmaster

import androidx.navigation.NamedNavArgument
import io.github.hanihashemi.eggmaster.Screen.Timer.Destination


sealed class Screen(val route: String, val navArguments: List<NamedNavArgument> = emptyList()) {

    data object BoilDetail : Destination("boilDetail")

    data object Intro : Screen("intro") {
        sealed class Destination(val route: String) {
            data object Title : Destination("title")
            data object Tutorial : Destination("tutorial")

        }
    }

    data object Timer : Screen("timer") {
        sealed class Destination(val route: String) {
            data object BoilTimer : Destination("boilTimer")
        }
    }

    data object Finished : Screen("finished") {
        sealed class Destination(val route: String) {
            data object BoilFinish : Destination("boilFinish")
        }
    }
}