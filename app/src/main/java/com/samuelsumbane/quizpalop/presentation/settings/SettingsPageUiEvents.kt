package com.samuelsumbane.quizpalop.presentation.settings

sealed interface SettingsPageUiEvents {
    data object OnToggleSoundState : SettingsPageUiEvents
    data object OnToggleVibrateState : SettingsPageUiEvents
    data object OnTogglePostNotifications : SettingsPageUiEvents
}