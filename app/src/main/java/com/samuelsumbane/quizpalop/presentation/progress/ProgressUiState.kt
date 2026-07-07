package com.samuelsumbane.quizpalop.presentation.progress

import androidx.datastore.preferences.protobuf.LazyStringArrayList.emptyList
import com.samuelsumbane.quizpalop.domain.model.ProgressContentState
import com.samuelsumbane.quizpalop.domain.model.Question


data class ProgressUiState(
    val allQuestionsList: List<Question> = emptyList<Question>(),
//
    val lockLevelList: List<String> = emptyList(),
)
