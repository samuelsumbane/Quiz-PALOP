package org.quizpalop.app.domain.repository

interface DailyChallengeRepository {
    suspend fun loadActualDailyQuestionId(): String
    suspend fun saveActualDailyQuestionId(questionId: String)
    suspend fun loadSavedDailyQuestions(): Set<String>
    suspend fun saveDailyQuestions(newData: Set<String>)

    suspend fun loadLastDateTimeUserGotDailyQuestionId(): Long
    suspend fun saveLastDateTimeUserGotDailyQuestionId(datetime: Long)
}