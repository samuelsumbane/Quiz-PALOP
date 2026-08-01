package com.samuelsumbane.quizpalop.core.notifications

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class DailyChallengeWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun doWork(): Result {

        NotificationHelper(applicationContext)
            .showDailyChallenge()

        NotificationScheduler.scheduleDailyChallenge(
            context = applicationContext,
            hour = 8,
            minute = 0
        )

        return Result.success()
    }
}