package org.quizpalop.app.presentation.settings

data class SettingsPageUiState(
    val playSoundOnTap: Boolean = true,
    val vibrateOnTap: Boolean = true,
    val postNotifications: Boolean = true
)
