package io.github.hanihashemi.eggmaster

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.hanihashemi.eggmaster.MainViewModel.ViewAction.OnEggTemperaturePressed
import io.github.hanihashemi.eggmaster.MainViewModel.ViewAction.TutorialNextPressed
import io.github.hanihashemi.eggmaster.MainViewModel.ViewAction.TutorialPreviousPressed
import io.github.hanihashemi.eggmaster.domain.BoilingTimeCalcUseCase
import io.github.hanihashemi.eggmaster.ui.models.EggBoiledType
import io.github.hanihashemi.eggmaster.ui.models.EggDetailsUiModel
import io.github.hanihashemi.eggmaster.ui.models.EggSize
import io.github.hanihashemi.eggmaster.ui.models.EggTemperature
import io.github.hanihashemi.eggmaster.ui.models.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val boilingTimeCalcUseCase: BoilingTimeCalcUseCase,
) : ViewModel() {

    private val _viewEvent: Channel<ViewEvent> = Channel()
    val viewEvent = _viewEvent.receiveAsFlow()
    private val internalState: MutableStateFlow<InternalState> = MutableStateFlow(InternalState())
    val viewState: StateFlow<UiState> = internalState
        .map { internalState -> generateUiState(internalState) }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = UiState(),
        )

    private fun generateUiState(internalState: InternalState): UiState {
        return UiState(
            tutorialCurrentStep = internalState.tutorialCurrentStep,
            eggDetails = internalState.eggDetails,
            dropEgg = internalState.tutorialCurrentStep >= 1,
        )
    }

    fun dispatch(action: ViewAction) {
        when (action) {
            is TutorialNextPressed -> onTutorialNextPressed()
            is TutorialPreviousPressed -> onTutorialPreviousPressed()
            is OnEggTemperaturePressed -> onEggTemperaturePressed(action.eggTemperature)
            is ViewAction.OnEggSizePressed -> onEggSizePressed(action.eggSize)
            is ViewAction.OnEggCountChanged -> onEggCountChanged(action.eggCount)
            is ViewAction.OnEggBoiledTypePressed -> onEggBoilPressed(action.eggBoiledType)
        }
    }

    private fun onEggBoilPressed(eggBoiledType: EggBoiledType) {
        internalState.update {
            it.copy(eggDetails = it.eggDetails.copy(boiledType = eggBoiledType))
        }
        updateBoilingTime()
    }

    private fun onEggCountChanged(eggCount: Int) {
        internalState.update {
            it.copy(eggDetails = it.eggDetails.copy(count = eggCount))
        }
        updateBoilingTime()
    }

    private fun onEggSizePressed(eggSize: EggSize) {
        internalState.update {
            it.copy(eggDetails = it.eggDetails.copy(size = eggSize))
        }
        updateBoilingTime()
    }

    private fun onEggTemperaturePressed(eggTemperature: EggTemperature) {
        internalState.update {
            it.copy(eggDetails = it.eggDetails.copy(temperature = eggTemperature))
        }
        updateBoilingTime()
    }

    private fun onTutorialNextPressed() {
        val currentStep = internalState.value.tutorialCurrentStep
        if (currentStep == 2) {
            _viewEvent.trySend(ViewEvent.OpenNextPage)
        } else {
            internalState.value = internalState.value.copy(tutorialCurrentStep = currentStep + 1)
        }
    }

    private fun onTutorialPreviousPressed() {
        val currentStep = internalState.value.tutorialCurrentStep
        if (currentStep == 0) {
            _viewEvent.trySend(ViewEvent.NavigateBack)
        } else {
            internalState.value = internalState.value.copy(tutorialCurrentStep = currentStep - 1)
        }
    }

    private fun updateBoilingTime() {
        viewModelScope.launch(Dispatchers.Default) {
            val eggDetails = internalState.value.eggDetails
            val boilingTime = boilingTimeCalcUseCase.run(
                BoilingTimeCalcUseCase.Params(
                    eggCount = eggDetails.count,
                    eggSize = eggDetails.size,
                    eggTemp = eggDetails.temperature,
                    boilType = eggDetails.boiledType,
                )
            )

            internalState.update {
                it.copy(eggDetails = it.eggDetails.copy(boilingTime = boilingTime))
            }
        }
    }

    data class InternalState(
        val tutorialCurrentStep: Int = 0,
        val eggDetails: EggDetailsUiModel = EggDetailsUiModel(),
    )

    sealed class ViewAction {
        data object TutorialNextPressed : ViewAction()
        data object TutorialPreviousPressed : ViewAction()
        data class OnEggTemperaturePressed(val eggTemperature: EggTemperature) : ViewAction()
        data class OnEggSizePressed(val eggSize: EggSize) : ViewAction()
        data class OnEggCountChanged(val eggCount: Int) : ViewAction()
        data class OnEggBoiledTypePressed(val eggBoiledType: EggBoiledType) : ViewAction()
    }

    sealed class ViewEvent {
        data object NavigateBack : ViewEvent()
        data object OpenNextPage : ViewEvent()
    }
}

