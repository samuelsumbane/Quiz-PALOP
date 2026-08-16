package org.quizpalop.app

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.quizpalop.app.core.notifications.AlarmScheduler
import org.quizpalop.app.core.notifications.NotificationChannels
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.quizpalop.app.core.SettingsManager
import org.quizpalop.app.core.notifications.DailyNotificationTime

class QuizPalop : Application() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@QuizPalop)
            modules(appModule)
        }

        NotificationChannels.create(this)
        val (hour, minute) = DailyNotificationTime.load(this)
        AlarmScheduler.scheduleDailyNotification(this, hour, minute)
    }
}