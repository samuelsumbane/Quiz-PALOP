package com.samuelsumbane.quizpalop.core

import DailyNotificationWorker
import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

fun agendarProximaNotificacao(context: Context) {
    val agora = Calendar.getInstance()
    val proximaExecucao = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 5)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        if (before(agora)) add(Calendar.DAY_OF_YEAR, 1)
    }

    val delay = proximaExecucao.timeInMillis - agora.timeInMillis

    val workRequest = OneTimeWorkRequestBuilder<DailyNotificationWorker>()
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .build()

    WorkManager.getInstance(context).enqueueUniqueWork(
        "notificacao_diaria",
        ExistingWorkPolicy.REPLACE,
        workRequest
    )
}