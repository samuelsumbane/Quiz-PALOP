package com.samuelsumbane.quizpalop.presentation.configquestions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelsumbane.quizpalop.domain.model.Category
import com.samuelsumbane.quizpalop.domain.model.Countries
import com.samuelsumbane.quizpalop.domain.model.PlayQuestionsNum
import com.samuelsumbane.quizpalop.domain.repository.QuizRepository
import com.samuelsumbane.quizpalop.domain.repository.SettingsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuestionsConfigViewModel(
    private val repo: QuizRepository,
    val settingsManager: SettingsManager
) : ViewModel() {
    private val _state = MutableStateFlow(QuestionsConfigUiState())

    val questionsConfigUiState = _state.asStateFlow()

    fun updateState(block: (QuestionsConfigUiState) -> QuestionsConfigUiState) = _state.update(block)

    fun setGameCategory(category: String) {
        viewModelScope.launch {
            updateState { it.copy(questionsCategory = Category.entries.first { c -> c.categoryName == category }) }
            settingsManager.saveStringValues(settingsManager.lastSelectedCategory, category)
        }
    }

    fun setGameCountry(country: String) {
        viewModelScope.launch {
            updateState { it.copy(questionsCountry = Countries.entries.first { c -> c.countryName == country } ) }
            settingsManager.saveStringValues(settingsManager.lastSelectedCountry, country)
        }
    }

    fun setGamePlayQuestionsLen(num: String) {
        updateState { it.copy(questionsCount = PlayQuestionsNum.entries.first { l -> l.num == num }) }
    }

    fun setQuestionConfig(questionsConfig: QuestionConfig) {
        updateState { it.copy(questionConfig = questionsConfig) }
    }

    fun readSavedCountry() {
        viewModelScope.launch {
            settingsManager.readStringValues(settingsManager.lastSelectedCountry).collect { lastSavedCountry ->
                Countries.entries.firstOrNull { it.countryName == lastSavedCountry }
                    ?.let { country ->
                        updateState {
                            it.copy(
                                questionsCountry = country,
                                lastCategoryWasSaved = true,
                            )
                        }
                    }
                    ?: run { settingsManager.saveIntValues(settingsManager.lives, 10) }

                updateState { it.copy(pageUiState = QuestionsConfigPageUiState.ShowContent) }
            }
        }
    }

    fun readSavedCategory() {
        viewModelScope.launch {
            settingsManager.readStringValues(settingsManager.lastSelectedCategory)
                .collect { lastCategory ->
                    if (lastCategory.isBlank()) {
                        settingsManager.saveIntValues(settingsManager.lives, intValue = 10)
                    }

                    updateState { it.copy(lastCategoryWasSaved = lastCategory.isNotBlank()) }
                    Countries.entries.firstOrNull { it.countryName == lastCategory }
                        ?.let { questionsCategory ->
                            updateState { it.copy(questionsCountry = questionsCategory) }
                        }
                }
        }
    }

    fun saveSelectedCountry(country: String) {
        viewModelScope.launch {
            settingsManager.saveStringValues(settingsManager.lastSelectedCountry, country)
        }
    }

    fun loadSavedQuestions() {
        viewModelScope.launch {
            val questions = repo.getQuestions()
            val savedQuestions = settingsManager.readSavedQuestionsList().first()
            updateState { it.copy(questions = questions, savedQuestions = savedQuestions) }
        }
    }

}