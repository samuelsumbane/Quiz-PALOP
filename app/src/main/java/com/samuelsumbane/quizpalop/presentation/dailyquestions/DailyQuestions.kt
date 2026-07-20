package com.samuelsumbane.quizpalop.presentation.dailyquestions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelsumbane.quizpalop.domain.model.Category
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


            val savedDailyQuestions = settingsManager.readSavedStringsValues(settingsManager.savedDailyQuestions).first()

            val allQuestionsId = allQuestions.map { it.id }.toSet()

            val allEasyQuestions = allQuestions.filter { it.questionLevel == "Easy" }.map { it.id }
            val allMediumQuestions = allQuestions.filter { it.questionLevel == "Medium" }.map { it.id }
            val allHardQuestions = allQuestions.filter { it.questionLevel == "Hard" }.map { it.id }

            val easySavedQuestions = allEasyQuestions intersect savedDailyQuestions
            val mediumSavedQuestions = allMediumQuestions intersect savedDailyQuestions
            val hardSavedQuestions = allHardQuestions intersect savedDailyQuestions

            val easyQuestionsPercentage = easySavedQuestions.size.toFloat() / allEasyQuestions.size
            val mediumQuestionsPercentage = mediumSavedQuestions.size.toFloat() / allMediumQuestions.size
            val hardQuestionsPercentage = hardSavedQuestions.size.toFloat() / allHardQuestions.size


            val allNotAnsweredQuestions = allQuestionsId subtract savedDailyQuestions
            if (allNotAnsweredQuestions.isEmpty()) {
                settingsManager.saveStringsValues(settingsManager.savedDailyQuestions, emptySet())
            }

            val availablesQuestionsId = when {
                mediumQuestionsPercentage < 1.0f -> allNotAnsweredQuestions subtract  easySavedQuestions
                hardQuestionsPercentage < 1.0f -> allNotAnsweredQuestions subtract hardSavedQuestions
                else -> allNotAnsweredQuestions
            }

            val randomedQuestion = allQuestions.first { it.id == availablesQuestionsId.random() }
            settingsManager.saveStringValues(settingsManager.actualDailyQuestionId, randomedQuestion.id)
        }
    }
}