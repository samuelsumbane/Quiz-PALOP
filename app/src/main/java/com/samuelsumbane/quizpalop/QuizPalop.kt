package com.samuelsumbane.quizpalop

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.samuelsumbane.quizpalop.core.agendarNotificacaoDiaria
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class QuizPalop : Application() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@QuizPalop)
            modules(appModule)
        }

        agendarNotificacaoDiaria(this)
    }
}