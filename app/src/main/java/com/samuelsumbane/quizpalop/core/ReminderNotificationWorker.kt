package com.samuelsumbane.quizpalop.core

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.samuelsumbane.quizpalop.R

class ReminderNotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override suspend fun doWork(): Result {
        mostrarNotificacao(applicationContext)
        return Result.success()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun mostrarNotificacao(context: Context) {

        val hasPermission = ActivityCompat.checkSelfPermission(
            context, Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            return
        }

        val notification = NotificationCompat.Builder(context, "qui_palop_reminders")
            .setSmallIcon(R.drawable.ic_launcher_monochrome)
            .setContentTitle("Quiz PALOP")
            .setContentText("Consegue responder está pergunta?")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(1001, notification)
        } catch (e: Exception) {
            Log.e("ReminderWorker", "Error trying to notify", e)
        }

    }
}