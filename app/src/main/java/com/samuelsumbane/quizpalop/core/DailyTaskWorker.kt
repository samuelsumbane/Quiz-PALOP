package com.samuelsumbane.quizpalop.core

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import java.util.Calendar
import java.util.concurrent.TimeUnit

class DailyTaskWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            executeDailyTask()

            scheduleNotificationFor9am(applicationContext)

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    private suspend fun executeDailyTask() {
        println("estado: The daily fun runned")
    }

    private fun scheduleNotificationFor9am(context: Context) {
        val delay = calculatDelayAt(20, 0)

        val notifRequest = OneTimeWorkRequestBuilder<ReminderNotificationWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "reminder_notification",
            ExistingWorkPolicy.REPLACE, 
            notifRequest
        )
    }
}

fun calculatDelayAt(hour: Int, minute: Int): Long {
    val now= Calendar.getInstance()
    val target = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        if (before(now)) add(Calendar.DAY_OF_YEAR, 1)
    }
    return target.timeInMillis - now.timeInMillis
}