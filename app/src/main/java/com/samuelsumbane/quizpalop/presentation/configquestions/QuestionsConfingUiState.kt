package com.samuelsumbane.quizpalop.presentation.configquestions

import com.samuelsumbane.quizpalop.domain.model.Category
import com.samuelsumbane.quizpalop.domain.model.Countries
import com.samuelsumbane.quizpalop.domain.model.Country
import com.samuelsumbane.quizpalop.domain.model.Question


data class QuestionsConfigUiState(
    val questionsCategory: Category = Category.History,
    val questionsCountry: Countries = Countries.Mz,

    val savedQuestions: Set<String> = emptySet(),
    val questions: List<Question> = emptyList(),

    val questionConfig: QuestionConfig = QuestionConfig.SelectCountry,
    val soundState: SoundState = SoundState.Playing,
    val lastCategoryWasSaved: Boolean = false,
    //
    val lockLevelList: List<String> = emptyList(),
)

enum class QuestionConfig(
    val pageTitle: String,
    val configOptionsList: List<String>,
) {
    SelectCountry(
        pageTitle = "Selecione o país",
        configOptionsList = Countries.entries.filter { it != Countries.None }.map { it.countryName }
    ),
    SelectCategory(
        pageTitle = "Selecione a catogoria",
        configOptionsList = Category.entries.map { it.categoryName }
    )
}

enum class SoundState { Mute, Playing }