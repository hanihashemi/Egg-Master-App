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
    fun `test boiling time`(testArgs: TestArgs) = with(testArgs) {
        val boilingTime = BoilingTimeCalcUseCase().run(
            BoilingTimeCalcUseCase.Params(
                eggTemp = temperature,
                eggSize = size,
                boilType = boiledType,
            )
        )

        assertEquals(
            actual = boilingTime.formatSecondsToMinutes(),
            expected = expectedBoilingTime,
        )
    }

    companion object {

        data class TestArgs(
            val temperature: EggTemperature,
            val size: EggSize,
            val boiledType: EggBoiledType,
            val expectedBoilingTime: String,
        )

        @JvmStatic
        fun provideTestArgs() = listOf(
            // Room Temperature
            // Small, Room, Soft
            TestArgs(
                temperature = EggTemperature.ROOM,
                size = EggSize.Small,
                boiledType = EggBoiledType.SOFT,
                expectedBoilingTime = "4:30",
            ),
            // Small, Room, Medium
            TestArgs(
                temperature = EggTemperature.ROOM,
                size = EggSize.Small,
                boiledType = EggBoiledType.MEDIUM,
                expectedBoilingTime = "5:50",
            ),
            // Small, Room, Hard
            TestArgs(
                temperature = EggTemperature.ROOM,
                size = EggSize.Small,
                boiledType = EggBoiledType.HARD,
                expectedBoilingTime = "7:10",
            ),

            // Fridge Temperature
            // Small, Fridge, Soft
            TestArgs(
                temperature = EggTemperature.FRIDGE,
                size = EggSize.Small,
                boiledType = EggBoiledType.SOFT,
                expectedBoilingTime = "5:50",
            ),
            // Small, Room, Medium
            TestArgs(
                temperature = EggTemperature.FRIDGE,
                size = EggSize.Small,
                boiledType = EggBoiledType.MEDIUM,
                expectedBoilingTime = "7:10",
            ),
            // Small, Room, Hard
            TestArgs(
                temperature = EggTemperature.FRIDGE,
                size = EggSize.Small,
                boiledType = EggBoiledType.HARD,
                expectedBoilingTime = "8:30",
            ),

            // Room Temperature
            // Medium, Room, Soft
            TestArgs(
                temperature = EggTemperature.ROOM,
                size = EggSize.Medium,
                boiledType = EggBoiledType.SOFT,
                expectedBoilingTime = "5:20",
            ),
            // Medium, Room, Medium
            TestArgs(
                temperature = EggTemperature.ROOM,
                size = EggSize.Medium,
                boiledType = EggBoiledType.MEDIUM,
                expectedBoilingTime = "6:40",
            ),
            // Medium, Room, Hard
            TestArgs(
                temperature = EggTemperature.ROOM,
                size = EggSize.Medium,
                boiledType = EggBoiledType.HARD,
                expectedBoilingTime = "8:00",
            ),

            // Fridge Temperature
            // Medium, Fridge, Soft
            TestArgs(
                temperature = EggTemperature.FRIDGE,
                size = EggSize.Medium,
                boiledType = EggBoiledType.SOFT,
                expectedBoilingTime = "6:40",
            ),
            // Medium, Room, Medium
            TestArgs(
                temperature = EggTemperature.FRIDGE,
                size = EggSize.Medium,
                boiledType = EggBoiledType.MEDIUM,
                expectedBoilingTime = "8:00",
            ),
            // Medium, Room, Hard
            TestArgs(
                temperature = EggTemperature.FRIDGE,
                size = EggSize.Medium,
                boiledType = EggBoiledType.HARD,
                expectedBoilingTime = "9:20",
            ),

            // Room Temperature
            // Large, Room, Soft
            TestArgs(
                temperature = EggTemperature.ROOM,
                size = EggSize.Large,
                boiledType = EggBoiledType.SOFT,
                expectedBoilingTime = "6:10",
            ),
            // Large, Room, Medium
            TestArgs(
                temperature = EggTemperature.ROOM,
                size = EggSize.Large,
                boiledType = EggBoiledType.MEDIUM,
                expectedBoilingTime = "7:30",
            ),
            // Medium, Room, Hard
            TestArgs(
                temperature = EggTemperature.ROOM,
                size = EggSize.Large,
                boiledType = EggBoiledType.HARD,
                expectedBoilingTime = "8:50",
            ),

            // Fridge Temperature
            // Large, Room, Soft
            TestArgs(
                temperature = EggTemperature.FRIDGE,
                size = EggSize.Large,
                boiledType = EggBoiledType.SOFT,
                expectedBoilingTime = "7:30",
            ),
            // Large, Room, Medium
            TestArgs(
                temperature = EggTemperature.FRIDGE,
                size = EggSize.Large,
                boiledType = EggBoiledType.MEDIUM,
                expectedBoilingTime = "8:50",
            ),
            // Medium, Room, Hard
            TestArgs(
                temperature = EggTemperature.FRIDGE,
                size = EggSize.Large,
                boiledType = EggBoiledType.HARD,
                expectedBoilingTime = "10:10",
            ),
        )
    }
}
