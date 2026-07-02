package com.samuelsumbane.quizpalop.presentation.userquestionspercentage

import com.samuelsumbane.quizpalop.domain.model.Question

data class UserQuestionsPercentageUiState(
    val questions: List<Question> = emptyList(),
    val savedQuestions: Set<String> = emptySet(),
    val lastCategoryWasSaved: Boolean = false,
    val easyAnsweredQuestionsList: Set<Int> = emptySet(),
    val easyAnsweredQuestionsPercent: Float = 0.0f, // Float is used in Element Width
    val mediumAnsweredQuestionsList: Set<Int> = emptySet(),
    val mediumAnsweredQuestionsPercent: Float = 0.0f,
    val hardAnsweredQuestionsList: Set<Int> = emptySet(),
    val hardAnsweredQuestionsPercent: Float = 0.0f,
)
