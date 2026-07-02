package com.samuelsumbane.quizpalop.presentation.configquestions

import androidx.datastore.preferences.protobuf.LazyStringArrayList.emptyList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelsumbane.quizpalop.domain.model.Category
import com.samuelsumbane.quizpalop.domain.model.Countries
import com.samuelsumbane.quizpalop.domain.model.Country
import com.samuelsumbane.quizpalop.domain.repository.QuizRepository
import com.samuelsumbane.quizpalop.domain.repository.SettingsManager
import com.samuelsumbane.quizpalop.presentation.userquestionspercentage.UserQuestionsPercentageUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

class QuestionsConfigViewModel(
    private val repo: QuizRepository,
    val settingsManager: SettingsManager
) : ViewModel() {
    private val _state = MutableStateFlow(QuestionsConfigUiState())
    private val _questionsPercentage = MutableStateFlow(UserQuestionsPercentageUiState())


    val questionsConfigUiState = _state.asStateFlow()
    val questionsPercentage = _questionsPercentage.asStateFlow()

    fun updateState(block: (QuestionsConfigUiState) -> QuestionsConfigUiState) = _state.update(block)

    fun setQuestionConfig(questionsConfig: QuestionConfig) {
        updateState { it.copy(questionConfig = questionsConfig) }
    }

    fun readSavedCategory() {
        viewModelScope.launch {
            settingsManager.readStringValues(settingsManager.lastSelectedCategory)
                .collect { lastCategory ->
                    if (lastCategory.isBlank()) {
                        settingsManager.saveIntValues(settingsManager.lives, intValue = 10)
                    }

//                    updateState { it.copy(lastCategoryWasSaved = lastCategory.isNotBlank()) }
                    Countries.entries.firstOrNull { it.countryName == lastCategory }
                        ?.let { questionsCategory ->
                            updateState { it.copy(questionsCountry = questionsCategory) }
                        }
                }
        }
    }

    fun readSavedLevel() {
        viewModelScope.launch {
            settingsManager.readStringValues(settingsManager.lastSelectedCountry)
                .collect { lastCategory ->
                    Category.entries.firstOrNull { it.categoryName == lastCategory }
                        ?.let { lastCategory ->
                            updateState { it.copy(questionsCategory = lastCategory) }
                        }
                }
        }
    }


    fun loadSavedQuestions() {
        viewModelScope.launch {
            val questions = repo.getQuestions()
            val savedQuestions = settingsManager.readSavedQuestionsList().first()
            updateState { it.copy(questions = questions, savedQuestions = savedQuestions) }
        }
    }

    fun levelForLocked() {
//        println("ouvindo: for lock ${progressUiState.value.easyAnsweredQuestionsPercent}")
        val lockLevelList = when {
            questionsPercentage.value.easyAnsweredQuestionsPercent < 1.0f -> listOf("Medium", "Hard")

            questionsPercentage.value.easyAnsweredQuestionsPercent >= 1.0f && questionsPercentage.value.mediumAnsweredQuestionsPercent <
                    1.0f -> listOf("Hard")

            else -> emptyList()
        }
        _state.update { it.copy(lockLevelList = lockLevelList) }
    }

}