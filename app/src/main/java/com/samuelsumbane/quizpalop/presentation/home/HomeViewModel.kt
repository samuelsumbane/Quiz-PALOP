package com.samuelsumbane.quizpalop.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelsumbane.quizpalop.domain.repository.SettingsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(private val settingsManager: SettingsManager) : ViewModel() {
    private val _state = MutableStateFlow(HomeUiState())
    val homeUiState = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val savedDailyQuestionId =
                settingsManager.readStringValues(settingsManager.actualDailyQuestionId).first()
            updateState { it.copy(dailyQuestionId =
                if (savedDailyQuestionId == "-1" || savedDailyQuestionId.isBlank()) null else  savedDailyQuestionId)
            }
        }
    }

    fun onEvent(event: HomeUiEvents) {
        when (event) {
            is HomeUiEvents.OnDismiss -> onDismissDailyQuestion(event.questionId)
            is HomeUiEvents.OnAcceptingToPlay -> onAcceptingPlayGame(event.questionId)
        }
    }
    fun updateState(block: (HomeUiState) -> HomeUiState) = _state.update(block)

    fun onDismissDailyQuestion(questionId: String) {
        viewModelScope.launch {
            updateState { it.copy(dailyQuestionId = null) }
            settingsManager.saveStringValues(settingsManager.actualDailyQuestionId, "-1")
        }
    }

    fun onAcceptingPlayGame(questionId: String) {
        viewModelScope.launch {

        }
    }
}