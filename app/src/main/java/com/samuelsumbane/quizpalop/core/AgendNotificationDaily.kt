package com.samuelsumbane.quizpalop.core

import DailyNotificationWorker
import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

fun agendarNotificacaoDiaria(context: Context) {
    val workRequest = PeriodicWorkRequestBuilder<DailyNotificationWorker>(
        repeatInterval = 1, TimeUnit.DAYS
    ).build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "notificacao_diaria",
        ExistingPeriodicWorkPolicy.KEEP,
        workRequest
    )
}