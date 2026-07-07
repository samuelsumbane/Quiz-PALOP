package com.samuelsumbane.quizpalop

import com.samuelsumbane.quizpalop.data.repository.QuizRepositoryImpl
import com.samuelsumbane.quizpalop.domain.repository.QuizRepository
import com.samuelsumbane.quizpalop.domain.repository.SettingsManager
import com.samuelsumbane.quizpalop.presentation.configquestions.QuestionsConfigViewModel
import com.samuelsumbane.quizpalop.presentation.maingamepage.MainGameViewModel
import com.samuelsumbane.quizpalop.presentation.progress.ProgressViewModel
import com.samuelsumbane.quizpalop.presentation.userquestionspercentage.UserQuestionsPercentageViewModel
import org.koin.android.ext.koin.androidContext

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<QuizRepository> { QuizRepositoryImpl(androidContext()) }
    single { SettingsManager(androidContext()) }
    viewModel { MainGameViewModel(get(), get()) }
    viewModel { UserQuestionsPercentageViewModel(get(), get()) }
    viewModel { QuestionsConfigViewModel(get(), get()) }
    viewModel { ProgressViewModel(get(), get()) }
}