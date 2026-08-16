package org.quizpalop.app.domain.repository

interface LifeNotificationScheduler {
    fun scheduleNotification(triggerAtMillis: Long)
    fun cancelNotification()
}