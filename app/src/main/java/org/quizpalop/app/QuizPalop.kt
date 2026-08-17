package org.quizpalop.app

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.quizpalop.app.core.notifications.AlarmScheduler
import org.quizpalop.app.core.notifications.DailyNotificationTime
import org.quizpalop.app.core.notifications.NotificationChannels

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