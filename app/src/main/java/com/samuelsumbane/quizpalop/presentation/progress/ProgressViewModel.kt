package com.samuelsumbane.quizpalop.presentation.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelsumbane.quizpalop.domain.model.Category
import com.samuelsumbane.quizpalop.domain.model.Countries
import com.samuelsumbane.quizpalop.domain.model.ProgressContentState
import com.samuelsumbane.quizpalop.domain.repository.QuizRepository
import com.samuelsumbane.quizpalop.domain.repository.SettingsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProgressViewModel(
    private val repo: QuizRepository,
    val settingsManager: SettingsManager
) : ViewModel() {
    val _state = MutableStateFlow(ProgressUiState())
    val progressUiState = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val questions = repo.getQuestions()
            updateState { it.copy(allQuestionsList = questions) }
        }
    }


    fun updateState(block: (ProgressUiState) -> ProgressUiState) {
        _state.update(block)
    }


    fun resetProgress() {
        viewModelScope.launch {
            settingsManager.saveQuestionsList(emptySet())
            settingsManager.saveStringValues(settingsManager.lastSelectedCategory, Category.History.categoryName)
            settingsManager.saveStringValues(settingsManager.lastSelectedCountry, Countries.None.countryName)
            updateState {
                it.copy(
                    easyAnsweredQuestionsList = emptySet(),
                    mediumAnsweredQuestionsList = emptySet(),
                    hardAnsweredQuestionsList = emptySet(),
                    easyAnsweredQuestionsPercent = 0.0f,
                    mediumAnsweredQuestionsPercent = 0.0f,
                    hardAnsweredQuestionsPercent = 0.0f
                )
            }
            changeProgressContentStateTo(ProgressContentState.ShowContent)
        }
    }

    fun changeProgressContentStateTo(newState: ProgressContentState) {
        updateState { it.copy(progressContentState = newState) }
    }
}