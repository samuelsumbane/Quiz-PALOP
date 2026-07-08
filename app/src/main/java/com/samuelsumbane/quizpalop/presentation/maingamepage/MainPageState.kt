package com.samuelsumbane.quizpalop.presentation.maingamepage

sealed class MainPageState {
    data object Loading : MainPageState()
    data object DisplayContent: MainPageState()
    data object QuestionsAnswered: MainPageState()
}