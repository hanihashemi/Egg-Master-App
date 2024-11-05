package io.github.hanihashemi.eggmaster.data.models

import io.github.hanihashemi.eggmaster.ui.models.EggBoiledType
import io.github.hanihashemi.eggmaster.ui.models.EggSize
import io.github.hanihashemi.eggmaster.ui.models.EggTemperature

data class EggDetailsDataModel(
    val temperature: EggTemperature = EggTemperature.FRIDGE,
    val size: EggSize = EggSize.Medium,
    val count: Int = 1,
    val boiledType: EggBoiledType = EggBoiledType.MEDIUM,
)

data class UserInfoDataModel(
    val userStep: ScreenStep = ScreenStep.TUTORIAL,
)

enum class ScreenStep {
    TUTORIAL,
    EGG_DETAILS,
}