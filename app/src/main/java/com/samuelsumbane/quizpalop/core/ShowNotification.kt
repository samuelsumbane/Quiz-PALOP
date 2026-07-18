package com.samuelsumbane.quizpalop.core

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.samuelsumbane.quizpalop.R

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun showNotification(context: Context, title: String, message: String) {
    val hasPermission = ActivityCompat.checkSelfPermission(
        context, Manifest.permission.POST_NOTIFICATIONS
    ) == PackageManager.PERMISSION_GRANTED

    Log.d("ReminderWorker", "Tem permissão? $hasPermission")

    if (!hasPermission) {
        Log.w("ReminderWorker", "Sem permissão, notificação não será mostrada")
        return
    }

    val notification = NotificationCompat.Builder(context, "qui_palop_reminders")
        .setSmallIcon(R.drawable.ic_launcher_monochrome)
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .build()

    try {
        NotificationManagerCompat.from(context).notify(1001, notification)
        Log.d("ReminderWorker", "notify() chamado com sucesso")
    } catch (e: Exception) {
        Log.e("ReminderWorker", "Erro ao notificar", e)
    }
}