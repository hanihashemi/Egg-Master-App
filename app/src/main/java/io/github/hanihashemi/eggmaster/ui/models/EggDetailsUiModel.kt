package io.github.hanihashemi.eggmaster.ui.models

data class EggDetailsUiModel(
    val temperature: EggTemperature = EggTemperature.FRIDGE,
    val size: EggSize = EggSize.Medium,
    val boiledType: EggBoiledType = EggBoiledType.MEDIUM,
    val boilingTime: Int = 360,
    val isEggTimerMode: Boolean = true,
) {
    val boilingTimeFinal: Int
        get() = if (isEggTimerMode) {
            boilingTime
        } else {
            boilingTime * 60
        }
}

enum class EggTemperature {
    ROOM,
    FRIDGE,
}

enum class EggSize {
    Small,
    Medium,
    Large,
}

enum class EggBoiledType {
    SOFT,
    MEDIUM,
    HARD,
}
