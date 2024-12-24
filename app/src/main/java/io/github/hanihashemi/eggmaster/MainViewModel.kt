package io.github.hanihashemi.eggmaster

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.hanihashemi.eggmaster.MainViewModel.ViewAction.OnEggTemperaturePressed
import io.github.hanihashemi.eggmaster.MainViewModel.ViewAction.TutorialNextPressed
import io.github.hanihashemi.eggmaster.MainViewModel.ViewAction.TutorialPreviousPressed
import io.github.hanihashemi.eggmaster.data.mappers.toDataModel
import io.github.hanihashemi.eggmaster.data.mappers.toUiModel
import io.github.hanihashemi.eggmaster.data.models.ScreenStep
import io.github.hanihashemi.eggmaster.data.models.UserInfoDataModel
import io.github.hanihashemi.eggmaster.data.preferences.EggMasterPreferences
import io.github.hanihashemi.eggmaster.domain.BoilingTimeCalcUseCase
import io.github.hanihashemi.eggmaster.ui.models.EggBoiledType
import io.github.hanihashemi.eggmaster.ui.models.EggDetailsUiModel
import io.github.hanihashemi.eggmaster.ui.models.EggSize
import io.github.hanihashemi.eggmaster.ui.models.EggTemperature
import io.github.hanihashemi.eggmaster.ui.models.EggTimerUiModel
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
    private val preferences: EggMasterPreferences,
) : ViewModel() {

    private val _viewEvent: Channel<ViewEvent> = Channel()
    val viewEvent = _viewEvent.receiveAsFlow()
    private val internalState: MutableStateFlow<InternalState> = MutableStateFlow(
        InternalState(
            eggDetails = getEggDetailsFromPreferences(),
            eggTimer = EggTimerUiModel(),
        )
    )
    val viewState: StateFlow<UiState> = internalState
        .map { internalState -> generateUiState(internalState) }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = UiState(
                eggDetails = getEggDetailsFromPreferences(),
                startDestination = getStartDestination(),
                eggTimer = EggTimerUiModel(),
            ),
        )

    private fun init(isTimerServiceRunning: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            preferences.setTimerServiceStatus(isTimerServiceRunning)
        }
    }

    private fun generateUiState(internalState: InternalState): UiState {
        return UiState(
            tutorialCurrentStep = internalState.tutorialCurrentStep,
            eggDetails = internalState.eggDetails,
            dropEgg = internalState.tutorialCurrentStep >= 1,
            startDestination = getStartDestination(),
            eggTimer = internalState.eggTimer,
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
            is ViewAction.UpdateBoilingTime -> updateBoilingTime()
            is ViewAction.StartTimer -> startTimer()
            is ViewAction.UpdateTimber -> updateTimer(action.time)
            is ViewAction.CancelTimer -> cancelTimer()
            is ViewAction.OnDonePressed -> onDonePressed()
            is ViewAction.OnDoneScreenShown -> onDoneScreenShown()
            is ViewAction.NavigateToTutorial -> navigateToTutorial()
            is ViewAction.NavigateToEggDetails -> navigateToEggDetails()
            is ViewAction.NavigateBack -> _viewEvent.trySend(ViewEvent.NavigateBack)
            is ViewAction.Init -> init(action.isTimerServiceRunning)
            is ViewAction.ResetTimerServiceEndTime -> resetTimerServiceEndTime()
        }
    }

    private fun resetTimerServiceEndTime() {
        viewModelScope.launch(Dispatchers.IO) {
            preferences.resetEndServiceTime()
        }
    }

    private fun onDonePressed() {
        _viewEvent.trySend(ViewEvent.CancelTimer)
    }

    private fun onDoneScreenShown() {
        viewModelScope.launch(Dispatchers.IO) {
            preferences.resetEndServiceTime()
        }
    }

    private fun cancelTimer() {
        _viewEvent.trySend(ViewEvent.CancelTimer)
    }

    private fun navigateToTutorial() {
        _viewEvent.trySend(ViewEvent.NavigateTo(Screen.Intro.Destination.Tutorial.route))
    }

    private fun navigateToEggDetails() {
        _viewEvent.trySend(ViewEvent.NavigateTo(Screen.BoilDetail.route))
    }

    private fun updateTimer(time: Int) {
        internalState.update {
            if (time == 0) {
                onFinishedTimer()
            }
            it.copy(eggTimer = it.eggTimer.copy(time = time))
        }
    }

    private fun onFinishedTimer() {
        _viewEvent.trySend(ViewEvent.NavigateTo(Screen.Finished.route, clearStack = true))
        viewModelScope.launch(Dispatchers.IO) {
            preferences.resetEndServiceTime()
        }
    }

    private fun startTimer() {
        val boilingTime = internalState.value.eggDetails.boilingTime
        _viewEvent.trySend(ViewEvent.StartTimerService(boilingTime))
    }

    private fun getStartDestination(): String {
        val isServiceRunning = preferences.isServiceRunning()
        val isServiceEndedInLastTenMinutes = preferences.isServiceEndedInLastTenMinutes()

        return when {
            isServiceRunning -> Screen.Timer.route
            isServiceEndedInLastTenMinutes -> Screen.Finished.route
            else -> Screen.Intro.route
        }
    }

    private fun getEggDetailsFromPreferences() = preferences.getEggDetails().toUiModel()

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
            viewModelScope.launch(Dispatchers.IO) {
                preferences.saveUserInfo(UserInfoDataModel(ScreenStep.EGG_DETAILS))
            }
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
        viewModelScope.launch(Dispatchers.IO) {
            val eggDetails = internalState.value.eggDetails
            preferences.saveEggDetails(eggDetails.toDataModel())
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
        val eggDetails: EggDetailsUiModel,
        val eggTimer: EggTimerUiModel,
    )

    sealed class ViewAction {
        data object TutorialNextPressed : ViewAction()
        data object TutorialPreviousPressed : ViewAction()
        data class OnEggTemperaturePressed(val eggTemperature: EggTemperature) : ViewAction()
        data class OnEggSizePressed(val eggSize: EggSize) : ViewAction()
        data class OnEggCountChanged(val eggCount: Int) : ViewAction()
        data class OnEggBoiledTypePressed(val eggBoiledType: EggBoiledType) : ViewAction()
        data object UpdateBoilingTime : ViewAction()
        data object StartTimer : ViewAction()
        data class UpdateTimber(val time: Int) : ViewAction()
        data object CancelTimer : ViewAction()
        data object OnDonePressed : ViewAction()
        data object OnDoneScreenShown : ViewAction()
        data object NavigateToTutorial : ViewAction()
        data object NavigateToEggDetails : ViewAction()
        data object NavigateBack : ViewAction()
        data class Init(val isTimerServiceRunning: Boolean) : ViewAction()
        data object ResetTimerServiceEndTime : ViewAction()
    }

    sealed class ViewEvent {
        data class StartTimerService(val boilingTime: Int) : ViewEvent()
        data object NavigateBack : ViewEvent()
        data object OpenNextPage : ViewEvent()
        data class NavigateTo(val route: String, val clearStack: Boolean = false) : ViewEvent()
        data object CancelTimer : ViewEvent()
    }
}
