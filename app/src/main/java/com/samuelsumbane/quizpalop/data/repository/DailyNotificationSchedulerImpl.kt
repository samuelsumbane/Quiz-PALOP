package com.samuelsumbane.quizpalop.data.repository

import android.content.Context
import com.samuelsumbane.quizpalop.core.notifications.AlarmScheduler
import com.samuelsumbane.quizpalop.domain.repository.DailyNotificationScheduler

class DailyNotificationSchedulerImpl(val context: Context) : DailyNotificationScheduler {
    override fun postDailyNotification() {
        AlarmScheduler.scheduleDailyNotification(context)
    }


    override fun cancelDailyNotification() {
        AlarmScheduler.cancelDailyNotification(context)
    }
}