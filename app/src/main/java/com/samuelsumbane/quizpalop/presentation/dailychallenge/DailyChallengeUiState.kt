package com.samuelsumbane.quizpalop.presentation.dailychallenge

import androidx.compose.ui.graphics.Color
import com.samuelsumbane.quizpalop.domain.model.Question
import com.samuelsumbane.quizpalop.presentation.maingamepage.GameTextMessage
import com.samuelsumbane.quizpalop.presentation.maingamepage.quizOptionDefaultColor

data class DailyChallengeUiState(
    val dailyQuestionId: String? = null,
    val dailyQuestion: Question? = null,
    val dailyQuestionRightAnswer: String = "",
    val gameTextMessage: GameTextMessage = GameTextMessage.Empty,
    val optionsColors: List<Color> = listOf(quizOptionDefaultColor, quizOptionDefaultColor, quizOptionDefaultColor, quizOptionDefaultColor)
)
