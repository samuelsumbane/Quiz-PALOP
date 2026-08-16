package org.quizpalop.app.data.repository

import kotlinx.coroutines.flow.first
import org.quizpalop.app.core.SettingsManager
import org.quizpalop.app.domain.repository.QuestionsConfigRepository

class QuestionsConfigRepositoryImpl(
    val settingsManager: SettingsManager
) : QuestionsConfigRepository{
    override suspend fun loadSavedCategory(): String {
        return settingsManager.readStringValues(settingsManager.lastSelectedCountry).first()
    }

    override suspend fun loadSavedCountry(): String {
        return settingsManager.readStringValues(settingsManager.lastSelectedCountry).first()
    }

    override suspend fun saveCategory(category: String) {
        settingsManager.saveStringValues(settingsManager.lastSelectedCategory, category)
    }

    override suspend fun saveCountry(country: String) {
        settingsManager.saveStringValues(settingsManager.lastSelectedCountry, country)
    }
}