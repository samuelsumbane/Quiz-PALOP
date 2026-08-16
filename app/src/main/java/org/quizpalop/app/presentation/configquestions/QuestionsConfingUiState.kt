package org.quizpalop.app.presentation.configquestions

import org.quizpalop.app.domain.model.Category
import org.quizpalop.app.domain.model.Countries
import org.quizpalop.app.domain.model.PlayQuestionsNum
import org.quizpalop.app.domain.model.Question


data class QuestionsConfigUiState(
    val questionsCategory: Category = Category.History,
    val questionsCountry: Countries = Countries.Angola,

    val savedQuestions: Set<String> = emptySet(),
    val questions: List<Question> = emptyList(),

    val questionConfig: QuestionConfig = QuestionConfig.SelectCountry,
    val soundState: SoundState = SoundState.Playing,
    val lastCategoryWasSaved: Boolean = false,
    val questionsCount: PlayQuestionsNum = PlayQuestionsNum.Ten,
    val pageUiState: QuestionsConfigPageUiState = QuestionsConfigPageUiState.Loading
)

enum class QuestionConfig(
    val pageTitle: String,
    val configOptionsList: List<String>,
) {
    SelectCountry(
        pageTitle = "Selecione o país",
        configOptionsList = Countries.entries.map { it.countryName }
    ),
    SelectCategory(
        pageTitle = "Selecione a categoria",
        configOptionsList = Category.entries.map { it.categoryName }
    ),
    SelectQuestionsLength(
        pageTitle = "Número de questões",
        configOptionsList = PlayQuestionsNum.entries.map { it.num }
    )
}

enum class SoundState { Mute, Playing }