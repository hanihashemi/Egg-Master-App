package io.github.hanihashemi.eggmaster

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _viewEvent: Channel<ViewEvent> = Channel()
    val viewEvent = _viewEvent.receiveAsFlow()
    private val internalState: MutableStateFlow<InternalState> = MutableStateFlow(InternalState())
    val viewState: StateFlow<UiState> = internalState
        .map { internalState -> generateUiState(internalState) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            UiState()
        )

    private fun generateUiState(internalState: InternalState): UiState {
        return UiState(
            tutorialCurrentStep = internalState.tutorialCurrentStep,
        )
    }

    fun dispatch(action: ViewAction) {
        when (action) {
            is ViewAction.TutorialNextPressed -> onTutorialNextPressed()
            is ViewAction.TutorialPreviousPressed -> onTutorialPreviousPressed()
        }
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

    data class UiState(
        val tutorialCurrentStep: Int = 0,
        val dropEgg: Boolean = false,
    )

    data class InternalState(
        val tutorialCurrentStep: Int = 0,
    )

    sealed class ViewAction {
        data object TutorialNextPressed : ViewAction()
        data object TutorialPreviousPressed : ViewAction()
    }

    sealed class ViewEvent {
        data object NavigateBack : ViewEvent()
        data object OpenNextPage : ViewEvent()
    }

}

