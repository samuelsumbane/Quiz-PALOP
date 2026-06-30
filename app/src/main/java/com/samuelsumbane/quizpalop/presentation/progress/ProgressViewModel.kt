//package com.samuelsumbane.quizpalop.presentation.progress
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.samuelsumbane.quizpalop.domain.model.ProgressContentState
//import com.samuelsumbane.quizpalop.domain.repository.QuizRepository
//import com.samuelsumbane.quizpalop.domain.repository.SettingsManager
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//
//class ProgressViewModel(
//    private val repo: QuizRepository,
//    val settingsManager: SettingsManager
//) : ViewModel() {
//    val _state = MutableStateFlow(ProgressUiState())
//    val progressUiState = _state.asStateFlow()
//
//    init {
//        viewModelScope.launch {
//            val questions = repo.getQuestions()
//            updateState { it.copy(allQuestionsList = questions) }
//        }
//    }
//
//    fun loadSavedQuestions(lockQuestionsList: Boolean) {
//        viewModelScope.launch {
//            val savedQuestions = settingsManager.readSavedQuestionsList().first()
//            val allEasyQuestions = questionslist.filter { it.level == "Easy" }.map { it.id }
//            val allMediumQuestions = questionslist.filter { it.level == "Medium" }.map { it.id }
//            val allHardQuestions = questionslist.filter { it.level == "Dificil" }.map { it.id }
//            //
//            val easySavedQuestions = allEasyQuestions intersect savedQuestions
//            val mediumSavedQuestions = allMediumQuestions intersect savedQuestions
//            val hardSavedQuestions = allHardQuestions intersect savedQuestions
//
//            val easyQuestionsPercentage = easySavedQuestions.size.toFloat() / allEasyQuestions.size
//            val mediumQuestionsPercentage = mediumSavedQuestions.size.toFloat() / allMediumQuestions.size
//            val hardQuestionsPercentage = hardSavedQuestions.size.toFloat() / allHardQuestions.size
//
//            updateState {
//                it.copy(
////                    hardAnsweredQuestionsList = hardQuestions,
//                    easyAnsweredQuestionsList = easySavedQuestions,
//                    easyAnsweredQuestionsPercent = easyQuestionsPercentage,
//                    mediumAnsweredQuestionsList = mediumSavedQuestions,
//                    mediumAnsweredQuestionsPercent = mediumQuestionsPercentage,
//                    hardAnsweredQuestionsList = hardSavedQuestions,
//                    hardAnsweredQuestionsPercent = hardQuestionsPercentage
//                )
//            }
//
//            if (lockQuestionsList) levelForLocked()
//            changeProgressContentStateTo(ProgressContentState.ShowContent)
//        }
//    }
//
//    fun updateState(block: (ProgressUiState) -> ProgressUiState) {
//        _state.update(block)
//    }
//
//    fun levelForLocked() {
////        println("ouvindo: for lock ${progressUiState.value.easyAnsweredQuestionsPercent}")
//        val lockLevelList = when {
//            progressUiState.value.easyAnsweredQuestionsPercent < 1.0f -> {
//                QuestionLevel.entries
//                    .filter { it != QuestionLevel.Easy }
//                    .map { it.value }
//            }
//
//            progressUiState.value.easyAnsweredQuestionsPercent >= 1.0f && progressUiState.value.mediumAnsweredQuestionsPercent <
//                    1.0f -> {
//                listOf(QuestionLevel.Hard.value)
//            }
//
//            else -> emptyList()
//        }
//        _state.update { it.copy(lockLevelList = lockLevelList) }
//    }
//
//    fun resetProgress() {
//        viewModelScope.launch {
//            settingsManager.saveQuestionsList(emptySet())
//            settingsManager.saveStringValues(settingsManager.lastSelectedCategory, "H")
//            settingsManager.saveStringValues(settingsManager.lastLevelSelected, "Easy")
//            updateState {
//                it.copy(
//                    easyAnsweredQuestionsList = emptySet(),
//                    mediumAnsweredQuestionsList = emptySet(),
//                    hardAnsweredQuestionsList = emptySet(),
//                    easyAnsweredQuestionsPercent = 0.0f,
//                    mediumAnsweredQuestionsPercent = 0.0f,
//                    hardAnsweredQuestionsPercent = 0.0f
//                )
//            }
//            changeProgressContentStateTo(ProgressContentState.ShowContent)
//        }
//    }
//
//    fun changeProgressContentStateTo(newState: ProgressContentState) {
//        updateState { it.copy(progressContentState = newState) }
//    }
//}