package org.quizpalop.app

import android.os.Build
import androidx.annotation.RequiresApi
import org.quizpalop.app.domain.repository.DailyChallengeRepository
import org.quizpalop.app.core.AndroidHapticManager
import org.quizpalop.app.core.HapticManager
import org.quizpalop.app.data.repository.DailyNotificationSchedulerImpl
import org.quizpalop.app.data.repository.LifeNotificationSchedulerImpl
import org.quizpalop.app.data.repository.QuizRepositoryImpl
import org.quizpalop.app.domain.repository.DailyNotificationScheduler
import org.quizpalop.app.domain.repository.LifeNotificationScheduler
import org.quizpalop.app.domain.repository.QuizRepository
import org.quizpalop.app.core.SettingsManager
import org.quizpalop.app.data.repository.UserPreferencesRepositoryImpl
import org.quizpalop.app.domain.repository.UserPreferencesRepository
import org.quizpalop.app.domain.usecase.DecreaseLifeUseCase
import org.quizpalop.app.presentation.configquestions.QuestionsConfigViewModel
import org.quizpalop.app.presentation.dailychallenge.DailyChallengeViewModel
import org.quizpalop.app.presentation.duel.DuelViewModel
import org.quizpalop.app.presentation.maingamepage.MainGameViewModel
import org.quizpalop.app.presentation.settings.SettingsPageViewModel
import org.quizpalop.app.presentation.userquestionspercentage.UserQuestionsPercentageViewModel
import org.koin.android.ext.koin.androidContext

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.quizpalop.app.data.repository.DailyChallengeRepositoryImpl
import org.quizpalop.app.data.repository.QuestionsConfigRepositoryImpl
import org.quizpalop.app.domain.repository.QuestionsConfigRepository

@RequiresApi(Build.VERSION_CODES.O)
val appModule = module {
    single<QuizRepository> { QuizRepositoryImpl(androidContext(), get()) }
    single { SettingsManager(androidContext()) }
    single<HapticManager> { AndroidHapticManager(androidContext()) }
    single<LifeNotificationScheduler> { LifeNotificationSchedulerImpl(androidContext()) }
    single<DailyNotificationScheduler> { DailyNotificationSchedulerImpl(androidContext()) }
    single<UserPreferencesRepository> { UserPreferencesRepositoryImpl(get()) }
    single<QuestionsConfigRepository> { QuestionsConfigRepositoryImpl(get()) }
    single<DailyChallengeRepository> { DailyChallengeRepositoryImpl(get()) }

// Domain
    factory { DecreaseLifeUseCase(get(), get()) }

    viewModel { MainGameViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { UserQuestionsPercentageViewModel(get(), get()) }
    viewModel { QuestionsConfigViewModel(get(), get(), get()) }
    viewModel { DuelViewModel(get(), get(), get()) }
    viewModel { DailyChallengeViewModel(get(), get(), get(), get()) }
    viewModel { SettingsPageViewModel(get(), get(), get()) }
}