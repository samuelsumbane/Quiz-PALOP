package com.samuelsumbane.quizpalop.domain.repository

interface DailyNotificationScheduler {
    fun postDailyNotification()
    fun cancelDailyNotification()
}