package com.samuelsumbane.quizpalop.data.repository

import android.content.Context
import com.samuelsumbane.quizpalop.core.loadQuestionsFromAssets
import com.samuelsumbane.quizpalop.domain.model.Pack
import com.samuelsumbane.quizpalop.domain.model.Question
import com.samuelsumbane.quizpalop.domain.repository.QuizRepository
import kotlinx.coroutines.async
import kotlinx.serialization.json.Json

class QuizRepositoryImpl(private val context: Context) : QuizRepository {

    override fun getPacks(): List<Pack> = allPacks

    override suspend fun getQuestions(): List<Question> {
        return loadQuestionsFromAssets(context)
    }
}