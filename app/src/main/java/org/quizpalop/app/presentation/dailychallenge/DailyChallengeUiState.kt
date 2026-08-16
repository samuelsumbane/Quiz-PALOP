package org.quizpalop.app.presentation.dailychallenge

import androidx.compose.ui.graphics.Color
import org.quizpalop.app.domain.model.Category
import org.quizpalop.app.domain.model.Countries
import org.quizpalop.app.domain.model.Question
import org.quizpalop.app.presentation.composables.PageUiState
import org.quizpalop.app.presentation.maingamepage.SoundState
import org.quizpalop.app.presentation.maingamepage.quizOptionDefaultColor

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
    val lastDateUserGotQuestion: Long = 0L,
    val dailyChallengeLoadQuestionState: DailyChallengeLoadQuestionState = DailyChallengeLoadQuestionState.LOADING,
    val soundState: SoundState = SoundState.Mute
)

enum class DailyChallengeLoadQuestionState { LOADING, FINISHED }