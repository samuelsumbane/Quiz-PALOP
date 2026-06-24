package com.samuelsumbane.quizpalop.presentation.maingamepage

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelsumbane.quizpalop.domain.repository.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainGameViewModel(private val repo: QuizRepository, ) : ViewModel() {

    private val _state = MutableStateFlow(MainGameUiState())
    val mainGameUiState = _state.asStateFlow()

    init {
        loadPacks()
        loadQuestions()
    }

    private fun updateState(block: (MainGameUiState) -> MainGameUiState) = _state.update(block)

    fun loadPacks() {
        val packs = repo.getPacks()
        updateState { it.copy(packs = packs) }
    }

    fun loadQuestions() {
        viewModelScope.launch {
            val questions = repo.getQuestions()
            updateState {
                it.copy(
                    questions = questions,
                    actualQuestion = questions.random(),
                    pageState = MainPageState.DisplayContent
                )
            }
        }
    }



}