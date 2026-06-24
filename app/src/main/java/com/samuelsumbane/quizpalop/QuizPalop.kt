package com.samuelsumbane.quizpalop

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class QuizPalop : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@QuizPalop)
            modules(appModule)
        }
    }
}