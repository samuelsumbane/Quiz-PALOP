package com.samuelsumbane.quizpalop.presentation.userquestionspercentage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
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
            val savedQuestions = settingsManager.readSavedQuestionsList().first()
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
            val savedQuestions = settingsManager.readSavedQuestionsList().first()
            calcLevelPercentage()
            updateState { it.copy(
                savedQuestions = savedQuestions,
                progressContentState = ProgressContentState.ShowContent
            ) }

        }
    }
}