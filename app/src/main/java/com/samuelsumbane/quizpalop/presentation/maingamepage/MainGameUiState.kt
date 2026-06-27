package com.samuelsumbane.quizpalop.presentation.maingamepage

import androidx.compose.ui.graphics.Color
import com.samuelsumbane.quizpalop.domain.model.AdState
import com.samuelsumbane.quizpalop.domain.model.Pack
import com.samuelsumbane.quizpalop.domain.model.Question
import com.samuelsumbane.quizpalop.domain.model.QuestionTimerState

data class MainGameUiState(
    val packs: List<Pack> = emptyList(),
    val questions: List<Question> = emptyList(),
    val actualQuestion: Question? = null,
    val actualQuestionRightAnswer: String = "",
    val optionsColors: List<Color> = listOf(quizOptionDefaultColor, quizOptionDefaultColor, quizOptionDefaultColor, quizOptionDefaultColor),
    val gameTextMessage: GameTextMessage = GameTextMessage.Empty,
    val lives: Int = 10,
    val userCoins: Int = 0,
    val pageState: MainPageState = MainPageState.Loading,

    val answeredQuestionsList: Set<String> = emptySet(),
    val loadSavedQuestionsFineshed: Boolean = false,
    val userGotHelp: Boolean = false,
    val questionsIdList: List<String> = emptyList(),
    val answeredQuestionsWithoutMistake: Int = 0,
    val lastRightOptionButtonDateTime: Long = 0L,

    //
    val questionsTimer: Int = 0,
    val timerState: QuestionTimerState = QuestionTimerState.Stop,
//    val sessionQuestionsLevel: QuestionLevel = QuestionLevel.Easy,
    //
    val lastDateTimeLostLives: Long = 0L,
    val currentQuestion: Question? = null,
    val adState: AdState = AdState.Loading
)
