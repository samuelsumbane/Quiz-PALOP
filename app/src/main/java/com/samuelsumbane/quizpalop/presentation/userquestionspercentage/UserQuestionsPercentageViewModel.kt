package com.samuelsumbane.quizpalop.presentation.userquestionspercentage

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

class UserQuestionsPercentageViewModel(
    val settingsManager: SettingsManager,
    val quizRepository: QuizRepository
) : ViewModel() {
    private val _state = MutableStateFlow(UserQuestionsPercentageUiState())
    val uiState = _state.asStateFlow()


    fun updateState(block: (UserQuestionsPercentageUiState) -> UserQuestionsPercentageUiState) =
        _state.update(block)

    fun calcLevelPercentage() {
        viewModelScope.launch {
            val savedQuestions = settingsManager.readSavedStringsValues(settingsManager.savedQuestionsList).first()
            val questionslist = quizRepository.getQuestions()

            val allEasyQuestions = questionslist.filter { it.questionLevel == "Easy" }.map { it.id }
            val allMediumQuestions =
                questionslist.filter { it.questionLevel == "Medium" }.map { it.id }
            val allHardQuestions = questionslist.filter { it.questionLevel == "Hard" }.map { it.id }

            val easySavedQuestions = allEasyQuestions intersect savedQuestions
            val mediumSavedQuestions = allMediumQuestions intersect savedQuestions
            val hardSavedQuestions = allHardQuestions intersect savedQuestions

            val easyQuestionsPercentage = easySavedQuestions.size.toFloat() / allEasyQuestions.size
            val mediumQuestionsPercentage =
                mediumSavedQuestions.size.toFloat() / allMediumQuestions.size
            val hardQuestionsPercentage = hardSavedQuestions.size.toFloat() / allHardQuestions.size

            updateState {
                it.copy(
                    questions = questionslist,
                  easyAnsweredQuestionsList = easySavedQuestions,
                    easyAnsweredQuestionsPercent = easyQuestionsPercentage,
                  mediumAnsweredQuestionsList = mediumSavedQuestions,
                    mediumAnsweredQuestionsPercent = mediumQuestionsPercentage,
                  hardAnsweredQuestionsList = hardSavedQuestions,
                    hardAnsweredQuestionsPercent = hardQuestionsPercentage
                )
            }
        }
    }

    fun loadSavedQuestions() {
        viewModelScope.launch {
            val savedQuestions = settingsManager.readSavedStringsValues(settingsManager.savedQuestionsList).first()
            calcLevelPercentage()
            updateState { it.copy(
                savedQuestions = savedQuestions,
                progressContentState = ProgressContentState.ShowContent
            ) }

        }
    }

    fun resetProgress() {
        viewModelScope.launch {
            settingsManager.saveStringsValues(settingsManager.savedQuestionsList, emptySet())
            settingsManager.saveStringValues(settingsManager.lastSelectedCategory, Category.History.categoryName)
            settingsManager.saveStringValues(settingsManager.lastSelectedCountry, Countries.Angola.countryName)
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

    fun levelForLocked() {
        val lockLevelList = when {
            uiState.value.easyAnsweredQuestionsPercent < 1.0f -> listOf(Category.Culture.categoryName, Category.Exam.categoryName)
            uiState.value.easyAnsweredQuestionsPercent >= 1.0f && uiState.value.mediumAnsweredQuestionsPercent <
                    1.0f -> listOf(Category.Exam.categoryName)

            else -> emptyList()
        }
        println("estado: ff${uiState.value.easyAnsweredQuestionsPercent} lc $lockLevelList")
        _state.update { it.copy(lockLevelList = lockLevelList) }
    }
}