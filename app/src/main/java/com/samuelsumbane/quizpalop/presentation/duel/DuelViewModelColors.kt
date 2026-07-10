package com.samuelsumbane.oremosquiz.presentation.duel

import androidx.compose.ui.graphics.Color
import com.samuelsumbane.oremosquiz.presentation.AppStates.defaultQuizOptionButtonColor
import com.samuelsumbane.quizpalop.presentation.duel.PlayerName


fun DuelViewModel.changePlayerButtonsColor(
    playerName: PlayerName,
    firstBtnColor: Color? = null,
    secondBtnColor: Color? = null,
    thirdBtnColor: Color? = null,
    fouthBtnColor: Color? = null,
) {
    firstBtnColor?.let { fColor -> updateState {
        if (playerName == PlayerName.FirstPlayer) it.copy(firstPlayer = it.firstPlayer.copy(firstBtnColor = fColor))
        else it.copy(secondPlayer = it.secondPlayer.copy(firstBtnColor = fColor))
    }}
    secondBtnColor?.let { sColor -> updateState {
        if (playerName == PlayerName.FirstPlayer) it.copy(firstPlayer = it.firstPlayer.copy(secondBtnColor = sColor))
        else it.copy(secondPlayer = it.secondPlayer.copy(secondBtnColor = sColor))
    }}
    thirdBtnColor?.let { tColor -> updateState {
        if (playerName == PlayerName.FirstPlayer) it.copy(firstPlayer = it.firstPlayer.copy(thirdBtnColor = tColor))
        else it.copy(secondPlayer = it.secondPlayer.copy(thirdBtnColor = tColor))
    }}
    fouthBtnColor?.let { fColor -> updateState {
        if (playerName == PlayerName.FirstPlayer) it.copy(firstPlayer = it.firstPlayer.copy(fouthBtnColor = fColor))
        else it.copy(secondPlayer = it.secondPlayer.copy(fouthBtnColor = fColor))
    }}
}

fun DuelViewModel.resetButtonsColors() {
    updateState {
        it.copy(
            firstPlayer = it.firstPlayer.copy(
                firstBtnColor = defaultQuizOptionButtonColor,
                secondBtnColor = defaultQuizOptionButtonColor,
                thirdBtnColor = defaultQuizOptionButtonColor,
                fouthBtnColor = defaultQuizOptionButtonColor,
            ),
            secondPlayer = it.secondPlayer.copy(
                firstBtnColor = defaultQuizOptionButtonColor,
                secondBtnColor = defaultQuizOptionButtonColor,
                thirdBtnColor = defaultQuizOptionButtonColor,
                fouthBtnColor = defaultQuizOptionButtonColor,
            )
        )
    }
}