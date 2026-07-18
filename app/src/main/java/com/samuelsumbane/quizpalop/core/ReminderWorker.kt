package com.samuelsumbane.quizpalop.core

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class ReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        println("lembrete: ReminderWorker Worker executando!")
        val title = inputData.getString(KEY_TITLE) ?: "Quiz PALOP"
        val message = inputData.getString(KEY_MESSAGE) ?: "Responda a pergunta de hoje"

        showNotification(applicationContext, title, message)

        return Result.success()
    }

    companion object {
        const val KEY_TITLE = "title"
        const val KEY_MESSAGE = "message"
    }
}