package org.quizpalop.app.presentation.maingamepage

import androidx.lifecycle.viewModelScope
import org.quizpalop.app.domain.model.HelpOption
import org.quizpalop.app.domain.model.UserCoins
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds

fun MainGameViewModel.helpWithFiftFift() {
    mainGameUiState.value.actualQuestion?.let { question ->
        if (mainGameUiState.value.userCoins < 15) {
            setGameTextMessage(GameTextMessage.CannotGetHelp("Não tem moedas suficientes", "Precisa de 15 moedas para remover as duas alternativas incorrectas.", HelpOption.FiftFift))
            return@let
        }
        val actualQuestion = question.options
        val rightAnswer = mainGameUiState.value.actualQuestionRightAnswer
        val allWrongAnswers = actualQuestion - rightAnswer
        val newOptions = listOf(rightAnswer, allWrongAnswers[0]).shuffled()
        updateState { it.copy(actualQuestion = it.actualQuestion?.copy(options = newOptions)) }
        changeUserCoins(UserCoins.DecreaseCoins(15))
    }
}

fun MainGameViewModel.changeUserHelpState(userGotHelp: Boolean) = updateState { it.copy(userGotHelp = userGotHelp) }

fun MainGameViewModel.helpWithRightOption() {
    val currentTimeMillis = System.currentTimeMillis()
    val passedHours = (currentTimeMillis - mainGameUiState.value.lastRightOptionButtonDateTime).milliseconds
    if (mainGameUiState.value.lastRightOptionButtonDateTime == 0L || passedHours.inWholeHours >= 24) {
        if (mainGameUiState.value.userCoins < 25) {
            setGameTextMessage(
                GameTextMessage.CannotGetHelp("Moedas insuficientes", "Precisa de 25 moedas para saber da resposta correcta",
                HelpOption.RightOption))
            return
        }
        mainGameUiState.value.actualQuestion?.let { question ->
            viewModelScope.launch {
                repo.saveLastDateTimeUserAskedRightOption(currentTimeMillis)
                updateState { it.copy(lastRightOptionButtonDateTime = currentTimeMillis) }
                setGameTextMessage(
                    GameTextMessage.ShowRightAnswer(
                        "A resposta correcta é:\n\n  ${mainGameUiState.value.actualQuestionRightAnswer}"
                    )
                )
            }
        }
        changeUserCoins(UserCoins.DecreaseCoins(25))
        changeUserHelpState(true)
    } else {
        val waitingTimeToRelease = 24.hours - passedHours
        setGameTextMessage(
            GameTextMessage.CannotGetHelp(
                "Recurso indisponível",
                reasonMessage = "Disponível novamente em ${waitingTimeToRelease.inWholeHours}h ${waitingTimeToRelease.inWholeMinutes % 60}m",
                HelpOption.RightOption
            )
        )
    }
}