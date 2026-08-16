package org.quizpalop.app.presentation.duel

import org.quizpalop.app.presentation.maingamepage.quizOptionDefaultColor


fun DuelViewModel.resetButtonsColors() {
    val defaultButtonsColors = listOf(quizOptionDefaultColor, quizOptionDefaultColor, quizOptionDefaultColor, quizOptionDefaultColor)
    updateState {
        it.copy(
            firstPlayer = it.firstPlayer.copy(optionsColors = defaultButtonsColors),
            secondPlayer = it.secondPlayer.copy(optionsColors = defaultButtonsColors)
        )
    }
}