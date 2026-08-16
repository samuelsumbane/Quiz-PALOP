package org.quizpalop.app.data.repository

import android.content.Context
import org.quizpalop.app.core.notifications.AlarmScheduler
import org.quizpalop.app.domain.repository.LifeNotificationScheduler

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