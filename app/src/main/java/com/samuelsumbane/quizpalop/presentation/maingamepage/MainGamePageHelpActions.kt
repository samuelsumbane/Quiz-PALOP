package com.samuelsumbane.quizpalop.presentation.maingamepage

fun MainGameViewModel.helpWithFiftFift() {
    mainGameUiState.value.actualQuestion?.let { question ->
        val actualQuestion = question.options
        val rightAnswer = mainGameUiState.value.actualQuestionRightAnswer
        val allWrongAnswers = actualQuestion - rightAnswer
        val newOptions = listOf(rightAnswer, allWrongAnswers[0]).shuffled()
        updateState { it.copy(actualQuestion = it.actualQuestion?.copy(options = newOptions)) }
    }
}