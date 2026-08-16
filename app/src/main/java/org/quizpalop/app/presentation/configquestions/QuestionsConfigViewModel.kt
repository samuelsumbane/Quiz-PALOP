package org.quizpalop.app.presentation.configquestions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.quizpalop.app.domain.model.Category
import org.quizpalop.app.domain.model.Countries
import org.quizpalop.app.domain.model.PlayQuestionsNum
import org.quizpalop.app.domain.repository.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.quizpalop.app.domain.repository.QuestionsConfigRepository

class QuestionsConfigViewModel(
    private val repo: QuizRepository,
    private val questionsConfigRepository: QuestionsConfigRepository,
    private val quizRepository: QuizRepository
) : ViewModel() {
    private val _state = MutableStateFlow(QuestionsConfigUiState())

    val questionsConfigUiState = _state.asStateFlow()

    fun updateState(block: (QuestionsConfigUiState) -> QuestionsConfigUiState) = _state.update(block)

    fun setGameCategory(category: String) {
        viewModelScope.launch {
            updateState { it.copy(questionsCategory = Category.entries.first { c -> c.categoryName == category }) }
            questionsConfigRepository.saveCategory(category)
        }
    }

    fun setGameCountry(country: String) {
        viewModelScope.launch {
            updateState { it.copy(questionsCountry = Countries.entries.first { c -> c.countryName == country } ) }
            questionsConfigRepository.saveCountry(country)
        }
    }

    fun setGamePlayQuestionsLen(num: String) {
        updateState { it.copy(questionsCount = PlayQuestionsNum.entries.first { l -> l.num == num }) }
    }

    fun setQuestionConfig(questionsConfig: QuestionConfig) {
        updateState { it.copy(questionConfig = questionsConfig) }
    }

    fun readSavedCountryAndCategory(loadCategory: Boolean = false) {
        viewModelScope.launch {
            if (loadCategory) {
                val lastCategory = questionsConfigRepository.loadSavedCategory()
                Category.entries.firstOrNull { it.categoryName == lastCategory }?.let { category ->
                    updateState { it.copy(questionsCategory = category) }
                }
            }

            val lastSavedCountry = questionsConfigRepository.loadSavedCountry()

            Countries.entries.firstOrNull { it.countryName == lastSavedCountry }
                ?.let { country ->
                    updateState {
                        it.copy(
                            questionsCountry = country,
                            lastCategoryWasSaved = true,
                        )
                    }
                }
                ?: run { quizRepository.saveUserLives(10) }

            updateState { it.copy(pageUiState = QuestionsConfigPageUiState.ShowContent) }
        }
    }

    fun readSavedCategory() {
        viewModelScope.launch {
            val lastCategory = questionsConfigRepository.loadSavedCategory()
            updateState { it.copy(lastCategoryWasSaved = lastCategory.isNotBlank()) }
            Countries.entries.firstOrNull { it.countryName == lastCategory }
                ?.let { questionsCategory ->
                    updateState { it.copy(questionsCountry = questionsCategory) }
                        }
        }
    }

    fun saveSelectedCountry(country: String) {
        viewModelScope.launch {
            questionsConfigRepository.saveCountry(country)
        }
    }

    fun loadSavedQuestions() {
        viewModelScope.launch {
            val questions = repo.getQuestions()
            val savedQuestions = quizRepository.loadSavedQuestionsList()
            updateState { it.copy(questions = questions, savedQuestions = savedQuestions) }
        }
    }

}