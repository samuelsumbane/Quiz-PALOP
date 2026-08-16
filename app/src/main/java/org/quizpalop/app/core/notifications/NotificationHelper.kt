package org.quizpalop.app.core.notifications

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import org.quizpalop.app.MainActivity
import org.quizpalop.app.R

class NotificationHelper(
    private val context: Context
) {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showDailyChallenge() {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(NotificationIntent.EXTRA_OPEN_DAILY_CHALLENGE, true)
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
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Quiz PALOP")
            .setContentText(messages.random())
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context)
            .notify(1, notification)
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showLifeRecovered() {

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            101,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        val messages = listOf(
            "❤️ Já tem uma vida disponível!",
            "🎮 Pode voltar a jogar!",
            "💚 Recuperou uma vida. Continue o desafio!",
            "⭐ A sua próxima vida já está pronta!"
        )

        val notification = NotificationCompat.Builder(
            context,
            NotificationChannels.DAILY_CHALLENGE
        )
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Quiz PALOP")
            .setContentText(messages.random())
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context)
            .notify(2, notification)
    }
}