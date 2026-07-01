package com.samuelsumbane.quizpalop.presentation.configquestions

import androidx.datastore.preferences.protobuf.LazyStringArrayList.emptyList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelsumbane.quizpalop.domain.model.Category
import com.samuelsumbane.quizpalop.domain.model.Countries
import com.samuelsumbane.quizpalop.domain.model.Country
import com.samuelsumbane.quizpalop.domain.repository.QuizRepository
import com.samuelsumbane.quizpalop.domain.repository.SettingsManager
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
    val questionsConfigUiState = _state.asStateFlow()

    fun updateState(block: (QuestionsConfigUiState) -> QuestionsConfigUiState) = _state.update(block)

    fun setCountry(country: Countries) {
        updateState { it.copy(questionsCountry = country) }
    }

    fun setCategory(category: Category) {
        updateState { it.copy(questionsCategory = category) }
    }

    fun loadQuestions() {
        viewModelScope.launch {
            val questions = repo.getQuestions()
            val savedQuestions = settingsManager.readSavedQuestionsList().first()
            updateState { it.copy(questions = questions, savedQuestions = savedQuestions) }
        }
    }

    fun calcLevelPercentage() {
        viewModelScope.launch {
            val savedQuestions = settingsManager.readSavedQuestionsList().first()
            val questionslist = questionsConfigUiState.value.questions

            val allEasyQuestions = questionslist.filter { it.questionLevel == "Easy" }.map { it.id }
            val allMediumQuestions = questionslist.filter { it.questionLevel == "Medium" }.map { it.id }
            val allHardQuestions = questionslist.filter { it.questionLevel == "Hard" }.map { it.id }
            //
            val easySavedQuestions = allEasyQuestions intersect savedQuestions
            val mediumSavedQuestions = allMediumQuestions intersect savedQuestions
            val hardSavedQuestions = allHardQuestions intersect savedQuestions

            val easyQuestionsPercentage = easySavedQuestions.size.toFloat() / allEasyQuestions.size
            val mediumQuestionsPercentage = mediumSavedQuestions.size.toFloat() / allMediumQuestions.size
            val hardQuestionsPercentage = hardSavedQuestions.size.toFloat() / allHardQuestions.size

            updateState {
                it.copy(
//                    easyAnsweredQuestionsList = easySavedQuestions,
                    easyAnsweredQuestionsPercent = easyQuestionsPercentage,
//                    mediumAnsweredQuestionsList = mediumSavedQuestions,
                    mediumAnsweredQuestionsPercent = mediumQuestionsPercentage,
//                    hardAnsweredQuestionsList = hardSavedQuestions,
                    hardAnsweredQuestionsPercent = hardQuestionsPercentage
                )
            }
        }
    }

    fun levelForLocked() {
//        println("ouvindo: for lock ${progressUiState.value.easyAnsweredQuestionsPercent}")
        val lockLevelList = when {
            questionsConfigUiState.value.easyAnsweredQuestionsPercent < 1.0f -> {
                listOf("Medium", "Hard")
            }

            questionsConfigUiState.value.easyAnsweredQuestionsPercent >= 1.0f && questionsConfigUiState.value.mediumAnsweredQuestionsPercent <
                    1.0f -> {
                listOf("Hard")
            }

            else -> emptyList()
        }
        _state.update { it.copy(lockLevelList = lockLevelList) }
    }

}