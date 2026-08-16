package org.quizpalop.app.domain.repository

interface DailyNotificationScheduler {
    fun postDailyNotification(hour: Int, minute: Int)
    fun cancelDailyNotification()
}