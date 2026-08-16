package org.quizpalop.app.presentation.settings

sealed interface SettingsPageUiEvents {
    data object OnToggleSoundState : SettingsPageUiEvents
    data object OnToggleVibrateState : SettingsPageUiEvents
    data object OnTogglePostNotifications : SettingsPageUiEvents
}