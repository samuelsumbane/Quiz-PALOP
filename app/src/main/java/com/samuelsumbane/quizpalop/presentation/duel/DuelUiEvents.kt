package com.samuelsumbane.quizpalop.presentation.duel

sealed interface DuelUiEvents {
    data class OnCheckPlayerResponse(val playerData: PlayerData, val clickedOptionName: String) : DuelUiEvents
    data object OnLoadNewDuelWithSameCategoryAndLevel : DuelUiEvents
}