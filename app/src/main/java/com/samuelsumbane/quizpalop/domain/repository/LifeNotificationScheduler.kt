package com.samuelsumbane.quizpalop.domain.repository

interface LifeNotificationScheduler {
    fun scheduleNotification(delayMillis: Long)
    fun cancelNotification()
}