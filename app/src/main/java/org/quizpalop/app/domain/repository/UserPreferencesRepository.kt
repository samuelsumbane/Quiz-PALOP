package org.quizpalop.app.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    suspend fun loadPlayOnTap(): Boolean
    suspend fun flowLoadPlayOnTap(): Flow<Boolean>
    suspend fun updatePlayOnTap(mobilePlay: Boolean)

    suspend fun loadVibrateOnTap(): Boolean
    suspend fun updateVibrateOnTap(mobileVibrate: Boolean)

    suspend fun loadPostNotifications(): Boolean
    suspend fun updatePostNotifications(postNotifications: Boolean)
}