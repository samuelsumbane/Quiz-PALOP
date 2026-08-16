package org.quizpalop.app.core.notifications

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.quizpalop.app.core.SettingsManager

class AlarmReceiver : BroadcastReceiver() {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context, intent: Intent) {
        val helper = NotificationHelper(context)
        when (intent.getStringExtra(NotificationIntent.EXTRA_TYPE)) {
            NotificationIntent.TYPE_DAILY -> {
                helper.showDailyChallenge()
                val (hour, minute) = DailyNotificationTime.load(context)
                AlarmScheduler.scheduleDailyNotification(context, hour, minute)
            }

            NotificationIntent.TYPE_LIFE -> helper.showLifeRecovered()
        }
    }
}
