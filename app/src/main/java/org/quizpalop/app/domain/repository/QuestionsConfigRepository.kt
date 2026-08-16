package org.quizpalop.app.domain.repository

interface QuestionsConfigRepository {
    suspend fun loadSavedCategory(): String
    suspend fun loadSavedCountry(): String
    suspend fun saveCategory(category: String)
    suspend fun saveCountry(country: String)
}