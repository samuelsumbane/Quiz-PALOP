package com.samuelsumbane.quizpalop.data.repository

import android.content.Context
import com.samuelsumbane.quizpalop.core.notifications.NotificationScheduler
import com.samuelsumbane.quizpalop.domain.repository.LifeNotificationScheduler

class LifeNotificationSchedulerImpl(
    private val context: Context
) : LifeNotificationScheduler {
    override fun scheduleNotification(delayMillis: Long) {
        NotificationScheduler.scheduleLifeNotification(context, delayMillis)
    }

    override fun cancelNotification() {
        NotificationScheduler.cancelLifeNotification(context)
    }
}