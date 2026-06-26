package com.samuelsumbane.quizpalop

import com.samuelsumbane.quizpalop.data.repository.QuizRepositoryImpl
import com.samuelsumbane.quizpalop.domain.repository.QuizRepository
import com.samuelsumbane.quizpalop.domain.repository.SettingsManager
import com.samuelsumbane.quizpalop.presentation.maingamepage.MainGameViewModel
import org.koin.android.ext.koin.androidContext

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<QuizRepository> { QuizRepositoryImpl(androidContext()) }
    single { SettingsManager(androidContext()) }
    viewModel { MainGameViewModel(get(), get()) }
}