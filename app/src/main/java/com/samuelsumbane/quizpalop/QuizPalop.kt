package com.samuelsumbane.quizpalop

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import com.samuelsumbane.quizpalop.core.notifications.NotificationChannels
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

        NotificationChannels.create(this)
    }
}