package org.quizpalop.app.data.repository

import org.quizpalop.app.core.SettingsManager
import org.quizpalop.app.domain.repository.DailyChallengeRepository
import kotlinx.coroutines.flow.first

class DailyChallengeRepositoryImpl(
    val settingsManager: SettingsManager
) : DailyChallengeRepository{
    override suspend fun loadActualDailyQuestionId(): String {
        return settingsManager.readStringValues(settingsManager.actualDailyQuestionId).first()
    }

    override suspend fun saveActualDailyQuestionId(questionId: String) {
        settingsManager.saveStringValues(settingsManager.actualDailyQuestionId, questionId)
    }

    override suspend fun loadSavedDailyQuestions(): Set<String> {
        return settingsManager.readSavedStringsValues(settingsManager.savedDailyQuestions).first()
    }

    override suspend fun saveDailyQuestions(newData: Set<String>) {
        settingsManager.saveStringsValues(settingsManager.savedDailyQuestions, newData)
    }

    override suspend fun loadLastDateTimeUserGotDailyQuestionId(): Long {
        return settingsManager.readLongValues(settingsManager.lastDateTimeUserGotDailyQuestionId)
    }

    override suspend fun saveLastDateTimeUserGotDailyQuestionId(datetime: Long) {
        settingsManager.saveLongValue(settingsManager.lastDateTimeUserGotDailyQuestionId, datetime)
    }
}