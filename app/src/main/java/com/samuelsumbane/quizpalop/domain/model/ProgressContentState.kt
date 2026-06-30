package com.samuelsumbane.quizpalop.domain.model

sealed class ProgressContentState {
    data object Loading : ProgressContentState()
    data object ShowContent : ProgressContentState()
    data class ConfirmExit(
        val title: String,
        val message: String
    ) : ProgressContentState()
}
