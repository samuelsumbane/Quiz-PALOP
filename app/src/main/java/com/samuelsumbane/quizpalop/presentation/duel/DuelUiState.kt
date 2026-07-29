package com.samuelsumbane.quizpalop.presentation.duel

import androidx.compose.ui.graphics.Color
import com.samuelsumbane.quizpalop.domain.model.Category
import com.samuelsumbane.quizpalop.domain.model.Countries
import com.samuelsumbane.quizpalop.domain.model.Question
import com.samuelsumbane.quizpalop.presentation.maingamepage.quizOptionDefaultColor

data class DuelUiState(
    val allQuestions: List<Question> = emptyList(),
    val firstPlayer: PlayerData = PlayerData(),
    val secondPlayer: PlayerData = PlayerData(),
    val country: Countries = Countries.Angola,
    val category: Category = Category.History,
    val duelQuestionsSize: Int = 0,
    val pageState: PageState = PageState.Loading,
    val mobileVibrate: Boolean = true,
)


data class PlayerData(
    val name: PlayerName = PlayerName.FirstPlayer,
    val question: Question? = null,
    val questionsIdList: Set<String> = emptySet(),
    val actualQuestion: Question? = null,
    val actualQuestionRightAnswer: String = "",

    val rightAnsweredQuestions: Int = 0,
    val playerTimer: Int = 60,
    val optionsColors: List<Color> = listOf(quizOptionDefaultColor, quizOptionDefaultColor, quizOptionDefaultColor, quizOptionDefaultColor),
)


enum class PlayerName { FirstPlayer, SecondPlayer }

enum class PageState { Loading, ShowContent, DisplayMessage }