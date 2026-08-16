package org.quizpalop.app.core.notifications

import android.content.Context
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.quizpalop.app.core.SettingsManager

object DailyNotificationTime {
    fun load(context: Context): Pair<Int, Int> {
        val settingsManager = SettingsManager(context)
        val hour = runBlocking { settingsManager.readIntValues(settingsManager.dailyNotificationHour, 9).first() }
        val minute = runBlocking { settingsManager.readIntValues(settingsManager.dailyNotificationMinute, 0).first() }
        return hour to minute
    }
}