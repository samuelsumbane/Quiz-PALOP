package org.quizpalop.app.domain.model

sealed class ProgressContentState {
    data object Loading : ProgressContentState()
    data object ShowContent : ProgressContentState()
    data class ConfirmResetProgress(
        val title: String,
        val message: String
    ) : ProgressContentState()
}
