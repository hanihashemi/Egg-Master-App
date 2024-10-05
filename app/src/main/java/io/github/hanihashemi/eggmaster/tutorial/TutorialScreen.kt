package io.github.hanihashemi.eggmaster.tutorial

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.hanihashemi.eggmaster.MainViewModel
import io.github.hanihashemi.eggmaster.R
import io.github.hanihashemi.eggmaster.components.BoilingPot
import io.github.hanihashemi.eggmaster.tutorial.components.InformationCards
import io.github.hanihashemi.eggmaster.tutorial.components.PageIndicator
import io.github.hanihashemi.eggmaster.ui.models.UiState
import io.github.hanihashemi.eggmaster.ui.theme.Dimens
import io.github.hanihashemi.eggmaster.ui.theme.EggMasterTheme

@Composable
fun TutorialScreen(
    state: UiState,
    dispatch: (MainViewModel.ViewAction) -> Unit,
) {
    Scaffold(
        topBar = { TopBar(state) },
        bottomBar = { BottomBar(state, dispatch) },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(paddingValues)
                .padding(Dimens.PaddingNormal),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                BoilingPot(
                    modifier = Modifier,
                    dropEgg = state.dropEgg,
                )
                Image(
                    modifier = Modifier.scale(1.2f),
                    painter = painterResource(id = R.drawable.ic_boiling_pot_background),
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
private fun TopBar(state: UiState) {
    PageIndicator(
        modifier = Modifier
            .padding(vertical = Dimens.PaddingNormal)
            .fillMaxWidth()
            .height(40.dp),
        currentPage = state.tutorialCurrentStep
    )
}

@Composable
private fun BottomBar(state: UiState, dispatch: (MainViewModel.ViewAction) -> Unit) {
    InformationCards(state.tutorialCurrentStep) {
        dispatch(MainViewModel.ViewAction.TutorialNextPressed)
    }
}

@Preview
@Composable
fun TutorialScreenPreview() {
    EggMasterTheme {
        TutorialScreen(state = UiState(), dispatch = {})
    }
}

@Preview(device = Devices.NEXUS_10)
@Composable
fun TutorialScreenNexus_10_Preview() {
    EggMasterTheme {
        TutorialScreen(state = UiState(), dispatch = {})
    }
}

@Preview(device = Devices.NEXUS_5)
@Composable
fun TutorialScreenNexus_5_Preview() {
    EggMasterTheme {
        TutorialScreen(state = UiState(), dispatch = {})
    }
}