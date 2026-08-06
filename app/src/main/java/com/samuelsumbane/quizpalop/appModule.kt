package com.samuelsumbane.quizpalop

import android.os.Build
import androidx.annotation.RequiresApi
import com.samuelsumbane.quizpalop.core.AndroidHapticManager
import com.samuelsumbane.quizpalop.core.HapticManager
import com.samuelsumbane.quizpalop.data.repository.DailyNotificationSchedulerImpl
import com.samuelsumbane.quizpalop.data.repository.LifeNotificationSchedulerImpl
import com.samuelsumbane.quizpalop.data.repository.QuizRepositoryImpl
import com.samuelsumbane.quizpalop.domain.repository.DailyNotificationScheduler
import com.samuelsumbane.quizpalop.domain.repository.LifeNotificationScheduler
import com.samuelsumbane.quizpalop.domain.repository.QuizRepository
import com.samuelsumbane.quizpalop.domain.repository.SettingsManager
import com.samuelsumbane.quizpalop.presentation.configquestions.QuestionsConfigViewModel
import com.samuelsumbane.quizpalop.presentation.dailychallenge.DailyChallengeViewModel
import com.samuelsumbane.quizpalop.presentation.duel.DuelViewModel
import com.samuelsumbane.quizpalop.presentation.maingamepage.MainGameViewModel
import com.samuelsumbane.quizpalop.presentation.settings.SettingsPageViewModel
import com.samuelsumbane.quizpalop.presentation.userquestionspercentage.UserQuestionsPercentageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

@RequiresApi(Build.VERSION_CODES.O)
val appModule = module {
    single<QuizRepository> { QuizRepositoryImpl(androidContext()) }
    single { SettingsManager(androidContext()) }
    single<HapticManager> { AndroidHapticManager(androidContext()) }
    single<LifeNotificationScheduler> { LifeNotificationSchedulerImpl(androidContext()) }
    single<DailyNotificationScheduler> { DailyNotificationSchedulerImpl(androidContext()) }

    viewModel { MainGameViewModel(get(), get(), get(), get()) }
    viewModel { UserQuestionsPercentageViewModel(get(), get()) }
    viewModel { QuestionsConfigViewModel(get(), get()) }
    viewModel { DuelViewModel(get(), get(), get()) }
    viewModel { DailyChallengeViewModel(get(), get(), get()) }
    viewModel { SettingsPageViewModel(get(), get(), get()) }
}