package org.quizpalop.app.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.quizpalop.app.domain.repository.DailyNotificationScheduler
import org.quizpalop.app.domain.repository.LifeNotificationScheduler
import org.quizpalop.app.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsPageViewModel(
    val dailyNotificationScheduler: DailyNotificationScheduler,
    val lifeNotificationScheduler: LifeNotificationScheduler,
    val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsPageUiState())
    val settingsUiState = _state.asStateFlow()
    init {
        viewModelScope.launch {
            val playSoundOnTap = userPreferencesRepository.loadPlayOnTap()
            val vibrateOnTap = userPreferencesRepository.loadVibrateOnTap()
            val postNotifications = userPreferencesRepository.loadPostNotifications()
            val dailyHour = userPreferencesRepository.loadDailyNotificationHour()
            val dailyMinute = userPreferencesRepository.loadDailyNotificationMinute()

            updateState {
                it.copy(
                    playSoundOnTap = playSoundOnTap,
                    vibrateOnTap = vibrateOnTap,
                    postNotifications = postNotifications,
                    dailyNotificationHour = dailyHour,
                    dailyNotificationMinute = dailyMinute
                )
            }
        }
    }

    fun onEvent(event: SettingsPageUiEvents) {
        when (event) {
            SettingsPageUiEvents.OnToggleSoundState -> toggleSoundState()
            SettingsPageUiEvents.OnToggleVibrateState -> toggleVibrateState()
            SettingsPageUiEvents.OnTogglePostNotifications -> togglePostNotifications()
            is SettingsPageUiEvents.OnChangeDailyNotificationTime -> changeDailyNotificationTime(event.hour, event.minute)
        }
    }

    fun changeDailyNotificationTime(hour: Int, minute: Int) {
        viewModelScope.launch {
            userPreferencesRepository.updateDailyNotificationTime(hour, minute)
            updateState { it.copy(dailyNotificationHour = hour, dailyNotificationMinute = minute) }
            if (settingsUiState.value.postNotifications) {
                dailyNotificationScheduler.postDailyNotification(hour, minute)
            }
        }
    }

    fun togglePostNotifications() {
        viewModelScope.launch {
            updateState { it.copy(postNotifications = !settingsUiState.value.postNotifications) }
            userPreferencesRepository.updatePostNotifications(settingsUiState.value.postNotifications)
            if (!settingsUiState.value.postNotifications) {
                dailyNotificationScheduler.cancelDailyNotification()
                lifeNotificationScheduler.cancelNotification()
            } else {
                dailyNotificationScheduler.postDailyNotification(
                    settingsUiState.value.dailyNotificationHour,
                    settingsUiState.value.dailyNotificationMinute
                )
            }
        }
    }

    fun updateState(block: (SettingsPageUiState) -> SettingsPageUiState) = _state.update(block)


    fun toggleSoundState() {
        viewModelScope.launch {
            updateState { it.copy(playSoundOnTap = !settingsUiState.value.playSoundOnTap) }
            userPreferencesRepository.updatePlayOnTap(settingsUiState.value.playSoundOnTap)
        }
    }

    fun toggleVibrateState() {
        viewModelScope.launch {
            updateState { it.copy(vibrateOnTap = !settingsUiState.value.vibrateOnTap) }
            userPreferencesRepository.updateVibrateOnTap(settingsUiState.value.vibrateOnTap)
        }
    }

}