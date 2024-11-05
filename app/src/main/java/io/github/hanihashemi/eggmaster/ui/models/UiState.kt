package io.github.hanihashemi.eggmaster.ui.models

data class UiState(
    val tutorialCurrentStep: Int = 0,
    val dropEgg: Boolean = false,
    val eggDetails: EggDetailsUiModel,
    val eggTimer: EggTimerUiModel,
    val startDestination: String,
)