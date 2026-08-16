package org.quizpalop.app.data.repository

import android.content.Context
import org.quizpalop.app.core.notifications.AlarmScheduler
import org.quizpalop.app.domain.repository.DailyNotificationScheduler

class DailyNotificationSchedulerImpl(val context: Context) : DailyNotificationScheduler {
    override fun postDailyNotification(hour: Int, minute: Int) {
        AlarmScheduler.scheduleDailyNotification(context, hour, minute)
    }
    override fun cancelDailyNotification() {
        AlarmScheduler.cancelDailyNotification(context)
    }
}