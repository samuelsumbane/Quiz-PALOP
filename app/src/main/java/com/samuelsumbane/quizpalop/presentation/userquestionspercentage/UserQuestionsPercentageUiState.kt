package com.samuelsumbane.quizpalop.presentation.userquestionspercentage

import com.samuelsumbane.quizpalop.domain.model.ProgressContentState
import com.samuelsumbane.quizpalop.domain.model.Question

data class UserQuestionsPercentageUiState(
    val questions: List<Question> = emptyList(),
    val savedQuestions: Set<String> = emptySet(),
    val lastCategoryWasSaved: Boolean = false,
    val easyAnsweredQuestionsList: Set<String> = emptySet(),
    val easyAnsweredQuestionsPercent: Float = 0.0f, // Float is used in Element Width
    val mediumAnsweredQuestionsList: Set<String> = emptySet(),
    val mediumAnsweredQuestionsPercent: Float = 0.0f,
    val hardAnsweredQuestionsList: Set<String> = emptySet(),
    val hardAnsweredQuestionsPercent: Float = 0.0f,
    val progressContentState: ProgressContentState = ProgressContentState.Loading
)
