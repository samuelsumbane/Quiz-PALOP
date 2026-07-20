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

    fun updateState(block: (HomeUiState) -> HomeUiState) = _state.update(block)

}