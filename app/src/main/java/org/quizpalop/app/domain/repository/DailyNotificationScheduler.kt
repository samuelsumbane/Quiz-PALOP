package org.quizpalop.app.domain.repository

interface DailyNotificationScheduler {
    fun postDailyNotification()
    fun cancelDailyNotification()
}