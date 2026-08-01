package com.samuelsumbane.quizpalop.core.notifications

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.samuelsumbane.quizpalop.MainActivity
import com.samuelsumbane.quizpalop.R

class NotificationHelper(
    private val context: Context
) {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showDailyChallenge() {

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            100,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        val messages = listOf(
            "🌍 O desafio diário já está disponível!",
            "🧠 Teste hoje os seus conhecimentos sobre os PALOP!",
            "⭐ Consegue acertar a pergunta de hoje?",
            "📚 Aprenda algo novo em apenas um minuto!"
        )

        val notification = NotificationCompat.Builder(
            context,
            NotificationChannels.DAILY_CHALLENGE
        )
            .setSmallIcon(R.drawable.ic_launcher_monochrome)
            .setContentTitle("Quiz PALOP")
            .setContentText(messages.random())
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context)
            .notify(1, notification)
    }
}