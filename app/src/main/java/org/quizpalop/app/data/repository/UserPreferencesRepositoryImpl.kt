package org.quizpalop.app.data.repository

import org.quizpalop.app.core.SettingsManager
import org.quizpalop.app.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class UserPreferencesRepositoryImpl(
    private val settingsManager: SettingsManager,
) : UserPreferencesRepository {
    override suspend fun loadPlayOnTap(): Boolean {
        return settingsManager.readBooleanValue(settingsManager.playSound).first()
    }

    override suspend fun flowLoadPlayOnTap(): Flow<Boolean> {
        return settingsManager.readBooleanValue(settingsManager.playSound)
    }

    override suspend fun updatePlayOnTap(mobilePlay: Boolean) {
        settingsManager.saveBooleanValues(settingsManager.playSound, mobilePlay)
    }

    override suspend fun loadVibrateOnTap(): Boolean {
        return settingsManager.readBooleanValue(settingsManager.vibrateOnTap).first()
    }

    override suspend fun updateVibrateOnTap(mobileVibrate: Boolean) {
        settingsManager.saveBooleanValues(settingsManager.vibrateOnTap, mobileVibrate)
    }

    override suspend fun loadPostNotifications(): Boolean {
        return settingsManager.readBooleanValue(settingsManager.postNotifications).first()
    }

    override suspend fun updatePostNotifications(postNotifications: Boolean) {
        settingsManager.saveBooleanValues(settingsManager.postNotifications, postNotifications)
    }

    override suspend fun loadDailyNotificationHour(): Int {
        return settingsManager.readIntValues(settingsManager.dailyNotificationHour, defaultValue = 9).first()
    }

    override suspend fun loadDailyNotificationMinute(): Int {
        return settingsManager.readIntValues(settingsManager.dailyNotificationMinute, defaultValue = 0).first()
    }

    override suspend fun updateDailyNotificationTime(hour: Int, minute: Int) {
        settingsManager.saveIntValues(settingsManager.dailyNotificationHour, hour)
        settingsManager.saveIntValues(settingsManager.dailyNotificationMinute, minute)
    }
}