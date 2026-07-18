package com.samuelsumbane.quizpalop.core

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.util.concurrent.TimeUnit

fun scheduleRecurringReminder(context: Context) {
    val data = workDataOf(
        ReminderWorker.KEY_TITLE to "Ripe Merge",
        ReminderWorker.KEY_MESSAGE to "Hora do seu desafio diário"
    )

    val request = PeriodicWorkRequestBuilder<ReminderWorker>(
        5, TimeUnit.HOURS
    ).build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "daily_reminder",
        ExistingPeriodicWorkPolicy.KEEP,
        request
    )
}
