package com.samuelsumbane.quizpalop.presentation.home

sealed interface HomeUiEvents {
    data class OnDismiss(val questionId: String) : HomeUiEvents
    data class OnAcceptingToPlay(val questionId: String) : HomeUiEvents
}