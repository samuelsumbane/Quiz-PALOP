package com.samuelsumbane.quizpalop.core

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

fun agendDailyTask(context: Context) {
    val initialDelay = calculatDelayAt(9, 48)

    val request = PeriodicWorkRequestBuilder<DailyTaskWorker>(24, TimeUnit.HOURS)
        .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "daily_task",
        ExistingPeriodicWorkPolicy.REPLACE,
        request
    )
}