package org.quizpalop.app.presentation.maingamepage

import org.quizpalop.app.domain.model.HelpOption

sealed interface MainGameUiEvents {
    data class OnCheckResponse(val clickedOptionName: String) : MainGameUiEvents
    data object OnExit : MainGameUiEvents
    data class OnHelp(val helpOption: HelpOption) : MainGameUiEvents
    data object OnToggleShowConfig : MainGameUiEvents
    data class OnToggleSoundState(val playSound: Boolean) : MainGameUiEvents
    data object OnToggleHapticState : MainGameUiEvents
}