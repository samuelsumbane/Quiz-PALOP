package com.samuelsumbane.quizpalop.presentation.progress

import androidx.datastore.preferences.protobuf.LazyStringArrayList.emptyList
import com.samuelsumbane.quizpalop.domain.model.ProgressContentState
import com.samuelsumbane.quizpalop.domain.model.Question


data class ProgressUiState(
    val allQuestionsList: List<Question> = emptyList<Question>(),
    val easyAnsweredQuestionsList: Set<Int> = emptySet(),
    val easyAnsweredQuestionsPercent: Float = 0.0f, // Float is used in Element Width
    val mediumAnsweredQuestionsList: Set<Int> = emptySet(),
    val mediumAnsweredQuestionsPercent: Float = 0.0f,
    val hardAnsweredQuestionsList: Set<Int> = emptySet(),
    val hardAnsweredQuestionsPercent: Float = 0.0f,
    //
    val lockLevelList: List<String> = emptyList(),
    val progressContentState: ProgressContentState = ProgressContentState.Loading
)
