package com.samuelsumbane.quizpalop.core.notifications

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission

class AlarmReceiver : BroadcastReceiver() {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        val helper = NotificationHelper(context)
        when (intent.getStringExtra(NotificationIntent.EXTRA_TYPE)) {

            NotificationIntent.TYPE_DAILY -> {
                helper.showDailyChallenge()
                AlarmScheduler.scheduleDailyNotification(context)
            }

            NotificationIntent.TYPE_LIFE -> {
                helper.showLifeRecovered()
            }
        }
    }

}
