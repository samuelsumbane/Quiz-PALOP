package com.samuelsumbane.quizpalop.presentation.settings

data class SettingsPageUiState(
    val playSoundOnTap: Boolean = true,
    val vibrateOnTap: Boolean = true,
    val postNotifications: Boolean = true
)
