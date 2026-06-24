package com.samuelsumbane.quizpalop.presentation.maingamepage

import androidx.compose.ui.graphics.Color
import com.samuelsumbane.quizpalop.domain.model.Pack
import com.samuelsumbane.quizpalop.domain.model.Question

data class MainGameUiState(
    val packs: List<Pack> = emptyList(),
    val questions: List<Question> = emptyList(),
    val actualQuestion: Question? = null,
    val optionsColors: List<Color> = listOf(quizOptionDefaultColor, quizOptionDefaultColor, quizOptionDefaultColor, quizOptionDefaultColor),
    val pageState: MainPageState = MainPageState.Loading
)
