package io.github.hanihashemi.eggmaster.tutorial

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.hanihashemi.eggmaster.MainViewModel
import io.github.hanihashemi.eggmaster.tutorial.components.BoilingPot
import io.github.hanihashemi.eggmaster.tutorial.components.InformationCards
import io.github.hanihashemi.eggmaster.tutorial.components.PageIndicator
import io.github.hanihashemi.eggmaster.ui.theme.Dimens
import io.github.hanihashemi.eggmaster.ui.theme.EggMasterTheme

@Composable
fun TutorialScreen(
    viewModel: MainViewModel = viewModel(),
) {
    val state by viewModel.viewState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column {
            PageIndicator(
                modifier = Modifier
                    .padding(vertical = Dimens.PaddingNormal)
                    .fillMaxWidth()
                    .height(40.dp),
                currentPage = state.tutorialCurrentStep
            )

            Box(
                modifier = Modifier
                    .weight(1F)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                BoilingPot(state.dropEgg)
            }

            InformationCards(state.tutorialCurrentStep) {
                viewModel.dispatch(MainViewModel.ViewAction.TutorialNextPressed)
            }
        }
    }
}

@Preview
@Composable
fun TutorialScreenPreview() {
    EggMasterTheme {
        TutorialScreen()
    }
}

@Preview(device = Devices.NEXUS_10)
@Composable
fun TutorialScreenNexus_10_Preview() {
    EggMasterTheme {
        TutorialScreen()
    }
}

@Preview(device = Devices.NEXUS_5)
@Composable
fun TutorialScreenNexus_5_Preview() {
    EggMasterTheme {
        TutorialScreen()
    }
}