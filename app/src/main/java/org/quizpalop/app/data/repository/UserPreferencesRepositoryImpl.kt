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
}