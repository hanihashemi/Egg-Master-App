package io.github.hanihashemi.eggmaster.data.mappers

import io.github.hanihashemi.eggmaster.data.models.EggDetailsDataModel
import io.github.hanihashemi.eggmaster.ui.models.EggDetailsUiModel

fun EggDetailsDataModel.toUiModel(): EggDetailsUiModel = EggDetailsUiModel(
    temperature = temperature,
    size = size,
    count = count,
    boiledType = boiledType,
)

fun EggDetailsUiModel.toDataModel(): EggDetailsDataModel = EggDetailsDataModel(
    temperature = temperature,
    size = size,
    count = count,
    boiledType = boiledType,
)