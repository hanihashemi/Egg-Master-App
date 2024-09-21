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
            count = params.eggCount,
        )
    }

    private fun calculateBoilingTime(
        size: Int,
        initialTemp: Int,
        yolkTemp: Int,
        count: Int,
    ): Int {
        val sizeFactor = 25
        val tempFactor = 50
        val yolkFactor = 60
        val countFactor = 15
        val baseTime = 210

        return baseTime +
                (size * sizeFactor) +
                (initialTemp * tempFactor) +
                (yolkTemp * yolkFactor) +
                (count * countFactor)
    }

    data class Params(
        val eggCount: Int,
        val eggSize: EggSize,
        val eggTemp: EggTemperature,
        val boilType: EggBoiledType,
    )
}