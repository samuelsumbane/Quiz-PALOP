package org.quizpalop.app.data.repository

import android.content.Context
import org.quizpalop.app.core.SettingsManager
import org.quizpalop.app.core.loadQuestionsFromAssets
import org.quizpalop.app.domain.model.Pack
import org.quizpalop.app.domain.model.Question
import org.quizpalop.app.domain.repository.QuizRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class QuizRepositoryImpl(
    private val context: Context,
    private val settingsManager: SettingsManager
) : QuizRepository {

    override fun getPacks(): List<Pack> = allPacks

    override suspend fun getQuestions(): List<Question> {
        val allQuestions = mutableListOf<Question>()
        for (question in listOf(
            "mozambique/mz_history.json", "mozambique/mz_culture.json", "mozambique/mz_exam.json",
            "angola/ao_history.json", "angola/ao_culture.json", "angola/ao_exam.json",
            "cape_verde/cv_history.json", "cape_verde/cv_culture.json", "cape_verde/exam.json",
            "guine_bissau/gw_history.json", "guine_bissau/gw_culture.json", "guine_bissau/gw_exam.json",
            "sao_tome_and_principe/stp_history.json", "sao_tome_and_principe/stp_culture.json", "sao_tome_and_principe/stp_exam.json"
        )) {
            val questions = loadQuestionsFromAssets(question, context)
            allQuestions.addAll(questions)
        }
        return allQuestions
    }

    override suspend fun saveUserCoins(newValue: Int) = settingsManager.saveIntValues(settingsManager.userCoins, newValue)

    override suspend fun saveUserLives(newValue: Int) = settingsManager.saveIntValues(settingsManager.lives, newValue)

    override suspend fun loadUserCoinsFlow(): Flow<Int> = settingsManager.readIntValues(settingsManager.userCoins)

    override suspend fun loadUserLivesFlow(): Flow<Int> = settingsManager.readIntValues(settingsManager.lives)

    override suspend fun loadLastDateTimeUserLostLives(): Long {
        return settingsManager.readLongValues(settingsManager.lastDateTimeLostLives)
    }

    override suspend fun saveLastDateTimeUserLostLives(datetime: Long) {
        settingsManager.saveLongValue(settingsManager.lastDateTimeLostLives, datetime)
    }

    override suspend fun loadLastDateTimeUserAskedRightOption(): Long {
        return settingsManager.readLongValues(settingsManager.lastRightOptionButtonDateTime)
    }

    override suspend fun saveLastDateTimeUserAskedRightOption(datetime: Long) {
        settingsManager.saveLongValue(settingsManager.lastRightOptionButtonDateTime, datetime)
    }

    override suspend fun loadSavedQuestionsList(): Set<String> {
        return settingsManager.readSavedStringsValues(settingsManager.savedQuestionsList).first()
    }

    override suspend fun saveQuestionsList(newData: Set<String>) {
        settingsManager.saveStringsValues(settingsManager.savedQuestionsList, newData)
    }

}