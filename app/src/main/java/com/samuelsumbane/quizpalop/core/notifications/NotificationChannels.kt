package com.samuelsumbane.quizpalop.core.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object NotificationChannels {

    const val DAILY_CHALLENGE = "daily_challenge"

    fun create(context: Context) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channel = NotificationChannel(
            DAILY_CHALLENGE,
            "Desafio Diário",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notificações do desafio diário."
        }

        context.getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)
    }
}