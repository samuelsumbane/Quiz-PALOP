package org.quizpalop.app.presentation.maingamepage

import org.quizpalop.app.domain.model.QuestionTimerState
import org.quizpalop.app.domain.model.SoundEvent

fun MainGameViewModel.setAddCoinsMessage(message: String) {
    sendSound(SoundEvent.CoinEarned)
    setGameTextMessage(GameTextMessage.AddedCoins(message))
}
fun MainGameViewModel.clearGameTextMessage() {
    updateState { it.copy(
        gameTextMessage = GameTextMessage.Empty,
        timerState = QuestionTimerState.Running
    ) }
}

fun MainGameViewModel.setGameTextMessage(gameMessage: GameTextMessage) {
    updateState{
        it.copy(
            timerState = if (gameMessage is GameTextMessage.Empty) QuestionTimerState.Running else QuestionTimerState.Stop,
            gameTextMessage = gameMessage
        )
    }
}

fun MainGameViewModel.onCloseMessageModal() {
    if (mainGameUiState.value.gameTextMessage is GameTextMessage.QuestionNotAnswered) {
//        startLoadingNextQuestion()
        loadNextQuestion()
    }
    clearGameTextMessage()
}
