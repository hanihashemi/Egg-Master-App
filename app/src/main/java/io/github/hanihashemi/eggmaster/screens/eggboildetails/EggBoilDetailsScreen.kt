package io.github.hanihashemi.eggmaster.screens.eggboildetails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.hanihashemi.eggmaster.MainViewModel
import io.github.hanihashemi.eggmaster.MainViewModel.ViewAction.OnEggBoiledTypePressed
import io.github.hanihashemi.eggmaster.MainViewModel.ViewAction.OnEggSizePressed
import io.github.hanihashemi.eggmaster.MainViewModel.ViewAction.OnEggTemperaturePressed
import io.github.hanihashemi.eggmaster.MainViewModel.ViewAction.StartTimer
import io.github.hanihashemi.eggmaster.MainViewModel.ViewAction.UpdateBoilingTime
import io.github.hanihashemi.eggmaster.R
import io.github.hanihashemi.eggmaster.components.OutlinedToggleButton
import io.github.hanihashemi.eggmaster.components.OutlinedToggleImageButton
import io.github.hanihashemi.eggmaster.components.RulerTimePicker
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

@Composable
fun EggBoilDetailsScreen(
    state: UiState,
    dispatch: (MainViewModel.ViewAction) -> Unit,
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val screenHeightDp = configuration.screenHeightDp.dp

    LaunchedEffect(Unit) {
        delay(500)
        dispatch(UpdateBoilingTime(null))
    }

    Scaffold(
        topBar = {
            TopBar(
                titleString = stringResource(R.string.egg_boil_details_screen_title),
                onBackClicked = { dispatch(MainViewModel.ViewAction.NavigateBack) },
            )
        },
        bottomBar = { BoilingTimeBottomBar(state.eggDetails.boilingTimeFinal) { dispatch(StartTimer) } },
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
                    .verticalScroll(rememberScrollState()),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = Dimens.PaddingXLarge),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    OutlinedToggleButton(
                        modifier = Modifier
                            .widthIn(120.dp),
                        isSelect = state.eggDetails.isEggTimerMode,
                        padding = PaddingValues(Dimens.PaddingXXSmall),
                        text = stringResource(R.string.egg_boil_details_screen_mode_egg_timer_label),
                        shape = RoundedCornerShape(50.dp, 0.dp, 0.dp, 50.dp),
                        smallFontSizes = true,
                        onClick = { dispatch(MainViewModel.ViewAction.OnTimerModeChanged(true)) },
                    )
                    OutlinedToggleButton(
                        modifier = Modifier
                            .widthIn(120.dp),
                        isSelect = !state.eggDetails.isEggTimerMode,
                        padding = PaddingValues(Dimens.PaddingXXSmall),
                        text = stringResource(R.string.egg_boil_details_screen_mode_custom_label),
                        smallFontSizes = true,
                        shape = RoundedCornerShape(0.dp, 50.dp, 50.dp, 0.dp),
                        onClick = { dispatch(MainViewModel.ViewAction.OnTimerModeChanged(false)) },
                    )
                }

                AnimatedVisibility(
                    modifier = Modifier
                        .requiredWidth(screenWidthDp)
                        .fillMaxHeight(),
                    visible = !state.eggDetails.isEggTimerMode
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Spacer(modifier = Modifier.height(screenHeightDp / 8))

                        HeadLine(
                            textBold = stringResource(R.string.egg_boil_details_screen_ruler_label_select_time),
                            addEggPrefix = false
                        )

                        RulerTimePicker(
                            value = state.eggDetails.boilingTime,
                            maxValue = 90,
                        ) { value ->
                            dispatch(UpdateBoilingTime(value))
                        }
                    }
                }

                AnimatedVisibility(visible = state.eggDetails.isEggTimerMode) {
                    Column {
                        HeadLine(
                            textBold = stringResource(R.string.egg_boil_details_screen_label_temperature),
                            noTopPadding = true
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                        ) {
                            OutlinedToggleButton(modifier = Modifier.widthIn(90.dp),
                                text = stringResource(R.string.egg_boil_details_screen_button_fridge_temperature),
                                isSelect = state.eggDetails.temperature == EggTemperature.FRIDGE,
                                onClick = {
                                    dispatch(OnEggTemperaturePressed(EggTemperature.FRIDGE))
                                })
                            Spacer(modifier = Modifier.width(Dimens.PaddingXXSmall))
                            OutlinedToggleButton(modifier = Modifier.widthIn(90.dp),
                                text = stringResource(R.string.egg_boil_details_screen_button_room_temperature),
                                isSelect = state.eggDetails.temperature == EggTemperature.ROOM,
                                onClick = {
                                    dispatch(OnEggTemperaturePressed(EggTemperature.ROOM))
                                })
                        }
                        HeadLine(textBold = stringResource(R.string.egg_boil_details_screen_label_size))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                        ) {
                            OutlinedToggleButton(modifier = Modifier.widthIn(90.dp),
                                text = stringResource(R.string.egg_boil_details_screen_button_size_small),
                                isSelect = state.eggDetails.size == EggSize.Small,
                                onClick = {
                                    dispatch(OnEggSizePressed(EggSize.Small))
                                })
                            OutlinedToggleButton(modifier = Modifier.widthIn(90.dp),
                                text = stringResource(R.string.egg_boil_details_screen_button_size_medium),
                                isSelect = state.eggDetails.size == EggSize.Medium,
                                onClick = {
                                    dispatch(OnEggSizePressed(EggSize.Medium))
                                })
                            OutlinedToggleButton(modifier = Modifier.widthIn(90.dp),
                                text = stringResource(R.string.egg_boil_details_screen_button_size_large),
                                isSelect = state.eggDetails.size == EggSize.Large,
                                onClick = {
                                    dispatch(OnEggSizePressed(EggSize.Large))
                                })
                        }
                        // Egg count does not affect boiling time!!
//                HeadLine(textBold = stringResource(R.string.egg_boil_details_screen_label_count))
//                Slider(modifier = Modifier.fillMaxWidth(),
//                    value = state.eggDetails.count.toFloat(),
//                    valueRange = 1f..10f,
//                    colors = SliderDefaults.colors(
//                        thumbColor = MaterialTheme.colorScheme.primary,
//                        activeTrackColor = MaterialTheme.colorScheme.primary,
//                        inactiveTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
//                    ),
//                    onValueChange = {
//                        dispatch(OnEggCountChanged(it.toInt()))
//                    },
//                    thumb = {
//                        Box {
//                            Image(
//                                modifier = Modifier.width(30.dp),
//                                painter = painterResource(id = R.drawable.ic_egg),
//                                contentDescription = stringResource(R.string.egg_boil_details_screen_slider_content_description),
//                            )
//
//                            Text(
//                                modifier = Modifier.align(Alignment.Center),
//                                style = MaterialTheme.typography.headlineMedium.copy(
//                                    fontWeight = FontWeight.Bold,
//                                    color = MaterialTheme.colorScheme.primary
//                                ),
//                                overflow = TextOverflow.Clip,
//                                text = "${state.eggDetails.count}",
//                            )
//                        }
//                    })
                        HeadLine(textBold = stringResource(R.string.egg_boil_details_screen_label_boiled_type))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                        ) {
                            OutlinedToggleImageButton(text = stringResource(R.string.egg_boil_details_screen_button_type_soft),
                                iconId = R.drawable.ic_egg_boiled_soft,
                                isSelect = state.eggDetails.boiledType == EggBoiledType.SOFT,
                                onClick = { dispatch(OnEggBoiledTypePressed(EggBoiledType.SOFT)) })
                            OutlinedToggleImageButton(text = stringResource(R.string.egg_boil_details_screen_button_type_medium),
                                iconId = R.drawable.ic_egg_boiled_medium,
                                isSelect = state.eggDetails.boiledType == EggBoiledType.MEDIUM,
                                onClick = { dispatch(OnEggBoiledTypePressed(EggBoiledType.MEDIUM)) })
                            OutlinedToggleImageButton(text = stringResource(R.string.egg_boil_details_screen_button_type_hard),
                                iconId = R.drawable.ic_egg_boiled_hard,
                                isSelect = state.eggDetails.boiledType == EggBoiledType.HARD,
                                onClick = { dispatch(OnEggBoiledTypePressed(EggBoiledType.HARD)) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeadLine(
    textBold: String,
    noTopPadding: Boolean = false,
    addEggPrefix: Boolean = true
) {
    val textPrefix = stringResource(R.string.egg_boil_details_screen_label_headline_prefix)
    Text(modifier = Modifier.padding(
        bottom = Dimens.PaddingNormal,
        top = if (noTopPadding) 0.dp else Dimens.PaddingXXLarge,
    ), style = MaterialTheme.typography.headlineMedium, text = buildAnnotatedString {
        if (addEggPrefix) {
            append(" $textPrefix")
        }
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(textBold)
        }
    })
}

@Composable
private fun EggBoilDetailsScreenPreview() {
    EggMasterTheme {
        EggBoilDetailsScreen(
            state = UiState(
                eggDetails = EggDetailsUiModel(isEggTimerMode = false),
                startDestination = "",
                eggTimer = EggTimerUiModel(),
            ), dispatch = { })
    }
}

@Preview(device = "id:pixel_9_pro")
@Composable
fun Pixel9Preview() = EggBoilDetailsScreenPreview()

@Preview(device = "id:medium_phone")
@Composable
fun MediumPreview() = EggBoilDetailsScreenPreview()

@Preview(device = "id:small_phone")
@Composable
fun SmallPreview() = EggBoilDetailsScreenPreview()

@Preview(device = "id:Nexus 9")
@Composable
fun TabletPreview() = EggBoilDetailsScreenPreview()