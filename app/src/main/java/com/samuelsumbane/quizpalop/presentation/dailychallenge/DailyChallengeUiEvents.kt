package com.samuelsumbane.quizpalop.presentation.dailychallenge

sealed interface DailyChallengeUiEvents {
    data class OnCheckResponse(val questionOption: String) : DailyChallengeUiEvents
}