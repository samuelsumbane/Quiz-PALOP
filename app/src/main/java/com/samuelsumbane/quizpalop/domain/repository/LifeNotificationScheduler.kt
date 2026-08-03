package com.samuelsumbane.quizpalop.domain.repository

interface LifeNotificationScheduler {
    fun scheduleNotification(triggerAtMillis: Long)
    fun cancelNotification()
}