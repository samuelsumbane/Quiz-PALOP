package com.samuelsumbane.quizpalop.core

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.util.concurrent.TimeUnit

fun scheduleDailyReminder(context: Context) {
    val request = PeriodicWorkRequestBuilder<ReminderWorker>(
        24, TimeUnit.HOURS
    ).setInputData(
        workDataOf(
            ReminderWorker.KEY_TITLE to "Ripe Merge",
            ReminderWorker.KEY_MESSAGE to "Não percas a tua streak diária!"
        )
    ).build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "daily_reminder",
        ExistingPeriodicWorkPolicy.KEEP,
        request
    )
}