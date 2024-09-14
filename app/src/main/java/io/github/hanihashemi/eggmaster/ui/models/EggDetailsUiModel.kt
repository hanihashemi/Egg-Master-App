package io.github.hanihashemi.eggmaster.ui.models

data class EggDetailsUiModel(
    val temperature: EggTemperature = EggTemperature.FRIDGE,
    val size: EggSize = EggSize.M,
    val count: Int = 1,
    val boiledType: EggBoiledType = EggBoiledType.MEDIUM,
)

enum class EggTemperature {
    FRIDGE,
    ROOM,
}

enum class EggSize {
    S,
    M,
    L,
}

enum class EggBoiledType {
    SOFT,
    MEDIUM,
    HARD,
}
