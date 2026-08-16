package org.quizpalop.app.presentation.duel

import androidx.compose.ui.graphics.Color
import org.quizpalop.app.domain.model.Category
import org.quizpalop.app.domain.model.Countries
import org.quizpalop.app.domain.model.Question
import org.quizpalop.app.presentation.maingamepage.quizOptionDefaultColor

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