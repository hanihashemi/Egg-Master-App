package io.github.hanihashemi.eggmaster.domain

import io.github.hanihashemi.eggmaster.ui.models.EggBoiledType
import io.github.hanihashemi.eggmaster.ui.models.EggSize
import io.github.hanihashemi.eggmaster.ui.models.EggTemperature
import javax.inject.Inject

class BoilingTimeCalcUseCase @Inject constructor() {

    fun run(params: Params): Int {
        return calculateBoilingTime(
            size = params.eggSize.ordinal,
            initialTemp = params.eggTemp.ordinal,
            yolkTemp = params.boilType.ordinal,
        )
    }

    private fun calculateBoilingTime(
        size: Int,
        initialTemp: Int,
        yolkTemp: Int,
    ): Int {
        val sizeFactor = 50
        val tempFactor = 80
        val yolkFactor = 80
        val baseTime = 270

        return baseTime +
                (size * sizeFactor) +
                (initialTemp * tempFactor) +
                (yolkTemp * yolkFactor)
    }

    data class Params(
        val eggSize: EggSize,
        val eggTemp: EggTemperature,
        val boilType: EggBoiledType,
    )
}