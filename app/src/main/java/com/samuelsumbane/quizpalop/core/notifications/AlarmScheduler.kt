package com.samuelsumbane.quizpalop.core.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.Calendar


object AlarmScheduler {

    private const val LIFE_REQUEST_CODE = 100
    private const val DAILY_REQUEST_CODE = 101

    private fun createLifePendingIntent(
        context: Context
    ): PendingIntent {

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(
                NotificationIntent.EXTRA_TYPE,
                NotificationIntent.TYPE_LIFE
            )
        }

        return PendingIntent.getBroadcast(
            context,
            LIFE_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )
    }


    fun scheduleLifeNotification(
        context: Context,
        triggerAtMillis: Long
    ) {
        val pendingIntent = createLifePendingIntent(context)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            pendingIntent
        )
    }


    fun cancelLifeNotification(
        context: Context
    ) {
        val pendingIntent = createLifePendingIntent(context)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.cancel(pendingIntent)
    }

    fun scheduleDailyNotification(
        context: Context,
        hour: Int = 9,
        minute: Int = 0
    ) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(
                NotificationIntent.EXTRA_TYPE,
                NotificationIntent.TYPE_DAILY
            )
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            DAILY_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    fun cancelDailyNotification(context: Context) {

        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(
                NotificationIntent.EXTRA_TYPE,
                NotificationIntent.TYPE_DAILY
            )
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            DAILY_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)
    }
}