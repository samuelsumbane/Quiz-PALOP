package com.samuelsumbane.quizpalop.data.repository

import android.content.Context
import com.samuelsumbane.quizpalop.core.notifications.AlarmScheduler
import com.samuelsumbane.quizpalop.domain.repository.LifeNotificationScheduler

class LifeNotificationSchedulerImpl(
    private val context: Context
) : LifeNotificationScheduler {
    override fun scheduleNotification(triggerAtMillis: Long) {
        AlarmScheduler.scheduleLifeNotification(context, triggerAtMillis)
    }

    override fun cancelNotification() {
        AlarmScheduler.cancelLifeNotification(context)
    }
}