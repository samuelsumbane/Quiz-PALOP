package org.quizpalop.app.presentation.maingamepage

import androidx.compose.ui.graphics.Color
import org.quizpalop.app.domain.model.AdState
import org.quizpalop.app.domain.model.Category
import org.quizpalop.app.domain.model.Countries
import org.quizpalop.app.domain.model.Pack
import org.quizpalop.app.domain.model.Question
import org.quizpalop.app.domain.model.QuestionTimerState

data class MainGameUiState(
    val packs: List<Pack> = emptyList(),
    val allQuestions: List<Question> = emptyList(),
//    val selectedQuestionsList: List<Question> = emptyList(),
    val actualQuestion: Question? = null,
    val actualQuestionRightAnswer: String = "",
    val optionsColors: List<Color> = listOf(
        quizOptionDefaultColor,
        quizOptionDefaultColor,
        quizOptionDefaultColor,
        quizOptionDefaultColor
    ),
    val gameTextMessage: GameTextMessage = GameTextMessage.Empty,
    val lives: Int? = null,
    val userCoins: Int = 0,
    val pageState: MainPageState = MainPageState.Loading,

    val answeredQuestionsList: Set<String> = emptySet(),
    val loadSavedQuestionsFineshed: Boolean = false,
    val userGotHelp: Boolean = false,
    val questionsIdList: Set<String> = emptySet(),
    val answeredQuestionsWithoutMistake: Int = 0,
    val lastRightOptionButtonDateTime: Long = 0L,
//
    val selectedCountry: Countries? = null,
    val selectedCategory: Category? = null,
    val questionsTimer: Int = 0,
    val timerState: QuestionTimerState = QuestionTimerState.Stop,
//    val sessionQuestionsLevel: QuestionLevel = QuestionLevel.Easy,
//    val pageUiState: PageUiState = PageUiState.Loading,
    val lastDateTimeLostLives: Long = 0L,
    val currentQuestion: Question? = null,
    val adState: AdState = AdState.Loading,
    val soundState: SoundState = SoundState.Playing,
    val mobileVibrate: Boolean = false,
    val showGameConfigs: Boolean = false,
)

enum class SoundState { Mute, Playing }


