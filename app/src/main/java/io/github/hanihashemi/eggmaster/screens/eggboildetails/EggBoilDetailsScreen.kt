package io.github.hanihashemi.eggmaster.screens.eggboildetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.hanihashemi.eggmaster.MainViewModel
import io.github.hanihashemi.eggmaster.MainViewModel.ViewAction.OnEggBoiledTypePressed
import io.github.hanihashemi.eggmaster.MainViewModel.ViewAction.OnEggCountChanged
import io.github.hanihashemi.eggmaster.MainViewModel.ViewAction.OnEggSizePressed
import io.github.hanihashemi.eggmaster.MainViewModel.ViewAction.OnEggTemperaturePressed
import io.github.hanihashemi.eggmaster.MainViewModel.ViewAction.StartTimer
import io.github.hanihashemi.eggmaster.MainViewModel.ViewAction.UpdateBoilingTime
import io.github.hanihashemi.eggmaster.R
import io.github.hanihashemi.eggmaster.components.OutlinedToggleButton
import io.github.hanihashemi.eggmaster.components.OutlinedToggleImageButton
import io.github.hanihashemi.eggmaster.components.TopBar
import io.github.hanihashemi.eggmaster.screens.eggboildetails.components.BoilingTimeBottomBar
import io.github.hanihashemi.eggmaster.ui.models.EggBoiledType
import io.github.hanihashemi.eggmaster.ui.models.EggDetailsUiModel
import io.github.hanihashemi.eggmaster.ui.models.EggSize
import io.github.hanihashemi.eggmaster.ui.models.EggTemperature
import io.github.hanihashemi.eggmaster.ui.models.EggTimerUiModel
import io.github.hanihashemi.eggmaster.ui.models.UiState
import io.github.hanihashemi.eggmaster.ui.theme.Dimens
import io.github.hanihashemi.eggmaster.ui.theme.EggMasterTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EggBoilDetailsScreen(
    state: UiState,
    dispatch: (MainViewModel.ViewAction) -> Unit,
) {
    LaunchedEffect(Unit) {
        delay(500)
        dispatch(UpdateBoilingTime)
    }

    Scaffold(
        topBar = { TopBar("Egg Boil Details") },
        bottomBar = { BoilingTimeBottomBar(state) { dispatch(StartTimer) } },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(paddingValues)
                .padding(Dimens.PaddingNormal)
        ) {
            HeadLine(textBold = "Temperature")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                OutlinedToggleButton(modifier = Modifier.widthIn(90.dp),
                    text = "Fridge temperature",
                    isSelect = state.eggDetails.temperature == EggTemperature.FRIDGE,
                    onClick = {
                        dispatch(OnEggTemperaturePressed(EggTemperature.FRIDGE))
                    })
                OutlinedToggleButton(modifier = Modifier.widthIn(90.dp),
                    text = "Room temperature",
                    isSelect = state.eggDetails.temperature == EggTemperature.ROOM,
                    onClick = {
                        dispatch(OnEggTemperaturePressed(EggTemperature.ROOM))
                    })
            }
            HeadLine(textBold = "Size")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                OutlinedToggleButton(modifier = Modifier.widthIn(90.dp),
                    text = "S",
                    isSelect = state.eggDetails.size == EggSize.Small,
                    onClick = {
                        dispatch(OnEggSizePressed(EggSize.Small))
                    })
                OutlinedToggleButton(modifier = Modifier.widthIn(90.dp),
                    text = "M",
                    isSelect = state.eggDetails.size == EggSize.Medium,
                    onClick = {
                        dispatch(OnEggSizePressed(EggSize.Medium))
                    })
                OutlinedToggleButton(modifier = Modifier.widthIn(90.dp),
                    text = "L",
                    isSelect = state.eggDetails.size == EggSize.Large,
                    onClick = {
                        dispatch(OnEggSizePressed(EggSize.Large))
                    })
            }
            HeadLine(textBold = "Count")
            Slider(modifier = Modifier.fillMaxWidth(),
                value = state.eggDetails.count.toFloat(),
                valueRange = 1f..10f,
                onValueChange = {
                    dispatch(OnEggCountChanged(it.toInt()))
                },
                thumb = {
                    Box {
                        Image(
                            modifier = Modifier.width(30.dp),
                            painter = painterResource(id = R.drawable.ic_egg),
                            contentDescription = "Boiling Pot",
                        )

                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            ),
                            overflow = TextOverflow.Clip,
                            text = "${state.eggDetails.count}",
                        )
                    }
                })
            HeadLine(textBold = "Boiled Type")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                OutlinedToggleImageButton(text = "Soft",
                    iconId = R.drawable.ic_egg_boiled_soft,
                    isSelect = state.eggDetails.boiledType == EggBoiledType.SOFT,
                    onClick = { dispatch(OnEggBoiledTypePressed(EggBoiledType.SOFT)) })
                OutlinedToggleImageButton(text = "Medium",
                    iconId = R.drawable.ic_egg_boiled_medium,
                    isSelect = state.eggDetails.boiledType == EggBoiledType.MEDIUM,
                    onClick = { dispatch(OnEggBoiledTypePressed(EggBoiledType.MEDIUM)) })
                OutlinedToggleImageButton(text = "Hard",
                    iconId = R.drawable.ic_egg_boiled_hard,
                    isSelect = state.eggDetails.boiledType == EggBoiledType.HARD,
                    onClick = { dispatch(OnEggBoiledTypePressed(EggBoiledType.HARD)) })
            }
        }
    }
}

@Composable
private fun HeadLine(text: String = "Egg ", textBold: String) {
    Text(modifier = Modifier.padding(
        bottom = Dimens.PaddingSmall,
        top = Dimens.PaddingNormal,
    ), style = MaterialTheme.typography.headlineMedium, text = buildAnnotatedString {
        append(text)
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(textBold)
        }
    })
}

@Composable
@Preview
private fun EggBoilDetailsScreenPreview() {
    EggMasterTheme {
        EggBoilDetailsScreen(state = UiState(
            eggDetails = EggDetailsUiModel(),
            startDestination = "",
            eggTimer = EggTimerUiModel(),
        ), dispatch = { })
    }
}