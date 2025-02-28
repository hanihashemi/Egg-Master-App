package io.github.hanihashemi.eggmaster.data.mappers

import io.github.hanihashemi.eggmaster.data.models.EggTimerDataModel
import io.github.hanihashemi.eggmaster.ui.models.EggTimerUiModel

fun EggTimerDataModel.toUiModel(): EggTimerUiModel = EggTimerUiModel(
    time = time.takeIf { it != 0 } ?: 5,
)

fun EggTimerUiModel.toDataModel(): EggTimerDataModel = EggTimerDataModel(
    time = time,
)