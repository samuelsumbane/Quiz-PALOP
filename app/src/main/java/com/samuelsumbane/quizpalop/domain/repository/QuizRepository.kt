package com.samuelsumbane.quizpalop.domain.repository

import com.samuelsumbane.quizpalop.domain.model.Answer
import com.samuelsumbane.quizpalop.domain.model.Pack
import com.samuelsumbane.quizpalop.domain.model.Question
import kotlinx.coroutines.flow.Flow

interface QuizRepository {
    fun getPacks(): List<Pack>
    suspend fun getQuestions(): List<Question>
}