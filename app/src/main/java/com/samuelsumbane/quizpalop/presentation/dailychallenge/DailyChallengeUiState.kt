package com.samuelsumbane.quizpalop.presentation.dailychallenge

import androidx.compose.ui.graphics.Color
import com.samuelsumbane.quizpalop.domain.model.Category
import com.samuelsumbane.quizpalop.domain.model.Countries
import com.samuelsumbane.quizpalop.domain.model.Question
import com.samuelsumbane.quizpalop.presentation.composables.PageUiState
import com.samuelsumbane.quizpalop.presentation.maingamepage.GameTextMessage
import com.samuelsumbane.quizpalop.presentation.maingamepage.quizOptionDefaultColor

data class DailyChallengeUiState(
    val dailyQuestionId: String? = null,
    val dailyQuestion: Question? = null,
    val questionCountry: Countries = Countries.Angola,
    val questionCategory: Category = Category.History,
    val dailyQuestionRightAnswer: String = "",
    val dailyChallengeMessage: DailyChallengeMessage = DailyChallengeMessage.Empty,
    val optionsColors: List<Color> = listOf(quizOptionDefaultColor, quizOptionDefaultColor, quizOptionDefaultColor, quizOptionDefaultColor),
    val pageUiState: PageUiState = PageUiState.Loading,
    val showBottomBar: Boolean = true,
)
