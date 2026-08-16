package org.quizpalop.app.domain.repository

import org.quizpalop.app.domain.model.Pack
import org.quizpalop.app.domain.model.Question
import kotlinx.coroutines.flow.Flow

interface QuizRepository {
    fun getPacks(): List<Pack>
    suspend fun getQuestions(): List<Question>

    suspend fun loadUserLivesFlow(): Flow<Int>
    suspend fun saveUserLives(newValue: Int)

    suspend fun loadUserCoinsFlow(): Flow<Int>
    suspend fun saveUserCoins(newValue: Int)

    suspend fun loadLastDateTimeUserLostLives(): Long
    suspend fun saveLastDateTimeUserLostLives(datetime: Long)
    suspend fun loadLastDateTimeUserAskedRightOption(): Long
    suspend fun saveLastDateTimeUserAskedRightOption(datetime: Long)
    suspend fun loadSavedQuestionsList(): Set<String>
    suspend fun saveQuestionsList(newData: Set<String>)
}
