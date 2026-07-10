package com.samuelsumbane.oremosquiz.presentation.duel

import com.samuelsumbane.quizpalop.presentation.duel.PlayerData

sealed interface DuelUiEvents {
    data class OnCheckPlayerResponse(val playerData: PlayerData, val clickedOption: Int) : DuelUiEvents
    data object OnLoadNewDuelWithSameCategoryAndLevel : DuelUiEvents
}