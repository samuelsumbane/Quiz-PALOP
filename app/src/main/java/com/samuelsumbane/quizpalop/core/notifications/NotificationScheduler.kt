package com.samuelsumbane.quizpalop.core.notifications

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

object NotificationScheduler {

    private const val WORK_NAME = "daily_challenge"

    fun scheduleDailyChallenge(
        context: Context,
        hour: Int = 8,
        minute: Int = 0
    ) {

        val delay = calculateDelay(hour, minute)

        val request =
            OneTimeWorkRequestBuilder<DailyChallengeWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                request
            )
    }

    fun cancel(context: Context) {
        WorkManager.getInstance(context)
            .cancelUniqueWork(WORK_NAME)
    }

    private fun calculateDelay(
        hour: Int,
        minute: Int
    ): Long {

        val now = Calendar.getInstance()

        val next = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (before(now)) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        return next.timeInMillis - now.timeInMillis
    }
}