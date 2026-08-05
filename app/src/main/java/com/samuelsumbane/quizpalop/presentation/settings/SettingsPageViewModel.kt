package com.samuelsumbane.quizpalop.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelsumbane.quizpalop.core.notifications.AlarmScheduler
import com.samuelsumbane.quizpalop.domain.repository.DailyNotificationScheduler
import com.samuelsumbane.quizpalop.domain.repository.LifeNotificationScheduler
import com.samuelsumbane.quizpalop.domain.repository.SettingsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsPageViewModel(
    val settingsManager: SettingsManager,
    val dailyNotificationScheduler: DailyNotificationScheduler,
    val lifeNotificationScheduler: LifeNotificationScheduler
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsPageUiState())
    val settingsUiState = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val playSoundOnTap = settingsManager.readBooleanValue(settingsManager.playSound).first()
            val vibrateOnTap = settingsManager.readBooleanValue(settingsManager.vibrateOnTap).first()
            val postNotifications = settingsManager.readBooleanValue(settingsManager.postNotifications).first()

            updateState {
                it.copy(
                    playSoundOnTap = playSoundOnTap,
                    vibrateOnTap = vibrateOnTap,
                    postNotifications = postNotifications
                )
            }
        }
    }

    fun updateState(block: (SettingsPageUiState) -> SettingsPageUiState) = _state.update(block)

    fun onEvent(event: SettingsPageUiEvents) {
        when (event) {
            SettingsPageUiEvents.OnToggleSoundState -> toggleSoundState()
            SettingsPageUiEvents.OnToggleVibrateState -> toggleVibrateState()
            SettingsPageUiEvents.OnTogglePostNotifications -> togglePostNotifications()
        }
    }

    fun toggleSoundState() {
        viewModelScope.launch {
            updateState { it.copy(playSoundOnTap = !settingsUiState.value.playSoundOnTap) }

           settingsManager.saveBooleanValues(settingsManager.playSound, settingsUiState.value.playSoundOnTap)
        }
    }

    fun toggleVibrateState() {
        viewModelScope.launch {
            updateState { it.copy(vibrateOnTap = !settingsUiState.value.vibrateOnTap) }

            settingsManager.saveBooleanValues(settingsManager.vibrateOnTap, settingsUiState.value.vibrateOnTap)
        }
    }

    fun togglePostNotifications() {
        viewModelScope.launch {
            updateState { it.copy(postNotifications = !settingsUiState.value.postNotifications) }
            settingsManager.saveBooleanValues(settingsManager.postNotifications, settingsUiState.value.postNotifications)
            if (!settingsUiState.value.postNotifications) {
                dailyNotificationScheduler.cancelDailyNotification()
                lifeNotificationScheduler.cancelNotification()
            } else {
                dailyNotificationScheduler.postDailyNotification()
            }
        }
    }
}