package io.github.hanihashemi.eggmaster.ui.models

data class EggDetailsUiModel(
    val temperature: EggTemperature = EggTemperature.FRIDGE,
    val size: EggSize = EggSize.Medium,
    val count: Int = 1,
    val boiledType: EggBoiledType = EggBoiledType.MEDIUM,
    val boilingTime: Int = 360,
)

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
