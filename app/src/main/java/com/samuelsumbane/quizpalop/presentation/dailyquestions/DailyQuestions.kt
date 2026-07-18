package com.samuelsumbane.quizpalop.presentation.dailyquestions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelsumbane.quizpalop.domain.repository.QuizRepository
import com.samuelsumbane.quizpalop.domain.repository.SettingsManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DailyQuestions(
    val repo: QuizRepository,
    val settingsManager: SettingsManager
) : ViewModel() {

    fun getAllSavedQuestions() {
        viewModelScope.launch {
            val allQuestions = repo.getQuestions()

            val savedDailyQuestions = settingsManager.readSavedStringsValues(settingsManager.dailyShownQuestionsId).first()
            val allQuestionsId = allQuestions.map { it.id }.toSet()
            val allNotAnsweredQuestions = allQuestionsId subtract savedDailyQuestions
            if (allNotAnsweredQuestions.isEmpty()) {
                settingsManager.saveStringsValues(settingsManager.dailyShownQuestionsId, emptySet())
                return@launch
            }

            val randomedQuestion = allNotAnsweredQuestions.random()

        }
    }
}