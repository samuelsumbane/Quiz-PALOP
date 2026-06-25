package com.samuelsumbane.quizpalop.presentation.maingamepage

import androidx.compose.ui.graphics.Color
import com.samuelsumbane.quizpalop.domain.model.Pack
import com.samuelsumbane.quizpalop.domain.model.Question

data class MainGameUiState(
    val packs: List<Pack> = emptyList(),
    val questions: List<Question> = emptyList(),
    val actualQuestion: Question? = null,
    val actualQuestionRightAnswer: String = "",
    val optionsColors: List<Color> = listOf(quizOptionDefaultColor, quizOptionDefaultColor, quizOptionDefaultColor, quizOptionDefaultColor),
    val gameTextMessage: GameTextMessage = GameTextMessage.Empty,
    val lives: Int = 0,
    val userCoins: Int = 0,
    val pageState: MainPageState = MainPageState.Loading
)
