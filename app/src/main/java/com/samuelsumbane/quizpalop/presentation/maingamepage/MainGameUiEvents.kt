package com.samuelsumbane.quizpalop.presentation.maingamepage

sealed interface MainGameUiEvents {
    data class OnCheckResponse(val clickedOptionName: String) : MainGameUiEvents
}