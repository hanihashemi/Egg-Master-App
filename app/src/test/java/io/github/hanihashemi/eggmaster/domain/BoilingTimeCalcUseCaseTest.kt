package io.github.hanihashemi.eggmaster.domain

import io.github.hanihashemi.eggmaster.extensions.formatSecondsToMinutes
import io.github.hanihashemi.eggmaster.ui.models.EggBoiledType
import io.github.hanihashemi.eggmaster.ui.models.EggSize
import io.github.hanihashemi.eggmaster.ui.models.EggTemperature
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

class BoilingTimeCalcUseCaseTest {

    @ParameterizedTest
    @MethodSource("provideTestArgs")
    fun `test boiling time`(testArgs: TestArgs) {
        val boilingTime = BoilingTimeCalcUseCase().run(
            BoilingTimeCalcUseCase.Params(
                eggTemp = testArgs.temperature,
                eggCount = testArgs.count,
                eggSize = testArgs.size,
                boilType = testArgs.boiledType,
            )
        )

        assertEquals(
            actual = boilingTime.formatSecondsToMinutes(),
            expected = testArgs.expectedBoilingTime,
        )
    }

    companion object {

        data class TestArgs(
            val temperature: EggTemperature,
            val size: EggSize,
            val count: Int,
            val boiledType: EggBoiledType,
            val expectedBoilingTime: String,
        )

        @JvmStatic
        fun provideTestArgs() = listOf(
            // Room, Small, Soft, 1
            TestArgs(
                temperature = EggTemperature.ROOM,
                size = EggSize.Small,
                count = 1,
                boiledType = EggBoiledType.SOFT,
                expectedBoilingTime = "3:45",
            ),
            // Fridge, Small, Soft, 1
            TestArgs(
                temperature = EggTemperature.FRIDGE,
                size = EggSize.Small,
                count = 1,
                boiledType = EggBoiledType.SOFT,
                expectedBoilingTime = "4:35",
            ),
            // Room, Medium, Soft, 1
            TestArgs(
                temperature = EggTemperature.ROOM,
                size = EggSize.Medium,
                count = 1,
                boiledType = EggBoiledType.SOFT,
                expectedBoilingTime = "4:10",
            ),
            // Room, Large, Soft, 1
            TestArgs(
                temperature = EggTemperature.ROOM,
                size = EggSize.Large,
                count = 1,
                boiledType = EggBoiledType.SOFT,
                expectedBoilingTime = "4:35",
            ),
            // Room, Small, MEDIUM, 1
            TestArgs(
                temperature = EggTemperature.ROOM,
                size = EggSize.Small,
                count = 1,
                boiledType = EggBoiledType.MEDIUM,
                expectedBoilingTime = "4:45",
            ),
            // Room, Small, HARD, 1
            TestArgs(
                temperature = EggTemperature.ROOM,
                size = EggSize.Small,
                count = 1,
                boiledType = EggBoiledType.HARD,
                expectedBoilingTime = "5:45",
            ),
            // Room, Small, Soft, 2
            TestArgs(
                temperature = EggTemperature.ROOM,
                size = EggSize.Small,
                count = 2,
                boiledType = EggBoiledType.SOFT,
                expectedBoilingTime = "4:00",
            ),
            // Room, Small, Soft, 3
            TestArgs(
                temperature = EggTemperature.ROOM,
                size = EggSize.Small,
                count = 3,
                boiledType = EggBoiledType.SOFT,
                expectedBoilingTime = "4:15",
            ),
        )
    }
}
