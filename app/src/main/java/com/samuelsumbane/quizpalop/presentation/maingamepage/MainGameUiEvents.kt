package com.samuelsumbane.quizpalop.presentation.maingamepage

import com.samuelsumbane.quizpalop.domain.model.HelpOption

sealed interface MainGameUiEvents {
    data class OnCheckResponse(val clickedOptionName: String) : MainGameUiEvents
    data object OnExit : MainGameUiEvents
    data class OnHelp(val helpOption: HelpOption) : MainGameUiEvents
    data object OnToggleShowConfig : MainGameUiEvents
}