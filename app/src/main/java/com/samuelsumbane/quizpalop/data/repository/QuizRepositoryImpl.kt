package com.samuelsumbane.quizpalop.data.repository

import android.content.Context
import com.samuelsumbane.quizpalop.core.loadQuestionsFromAssets
import com.samuelsumbane.quizpalop.domain.model.Pack
import com.samuelsumbane.quizpalop.domain.model.Question
import com.samuelsumbane.quizpalop.domain.repository.QuizRepository

class QuizRepositoryImpl(private val context: Context) : QuizRepository {

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
}