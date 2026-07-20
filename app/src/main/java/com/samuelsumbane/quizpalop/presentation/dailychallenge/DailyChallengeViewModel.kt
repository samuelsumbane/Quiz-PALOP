package com.samuelsumbane.quizpalop.presentation.dailychallenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelsumbane.quizpalop.domain.model.SoundEvent
import com.samuelsumbane.quizpalop.domain.repository.QuizRepository
import com.samuelsumbane.quizpalop.domain.repository.SettingsManager
import com.samuelsumbane.quizpalop.presentation.maingamepage.OptionState
import com.samuelsumbane.quizpalop.presentation.maingamepage.OptionsButton
import com.samuelsumbane.quizpalop.presentation.maingamepage.quizOptionCurrectButtonColor
import com.samuelsumbane.quizpalop.presentation.maingamepage.quizOptionWrongButtonColor
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DailyChallengeViewModel(
    private val settingsManager: SettingsManager,
    private val repo: QuizRepository
) : ViewModel() {
    private val _dailyChallengeViewModel = MutableStateFlow(DailyChallengeUiState())
    val dailychallengeUiState = _dailyChallengeViewModel.asStateFlow()
    private val _soundEvent = Channel<SoundEvent>()
    val soundEvent = _soundEvent.receiveAsFlow()


    fun updateState(block: (DailyChallengeUiState) -> DailyChallengeUiState) = _dailyChallengeViewModel.update(block)

    internal fun sendSound(sound: SoundEvent) = viewModelScope.launch { _soundEvent.send(sound) }


    fun onEvent(event: DailyChallengeUiEvents) {
        when (event) {
            is DailyChallengeUiEvents.OnCheckResponse -> onCheckResponse(event.questionOption)
        }
    }
    
    fun loadQuestion(questionId: String) {
        viewModelScope.launch {
            val allQuestions = repo.getQuestions()
            allQuestions.firstOrNull { it.id == questionId }?.let { question ->
                updateState { it.copy(dailyQuestion = question, dailyQuestionRightAnswer = question.options[question.correctIndex] ) }
            }
        }
    }

    fun updateButton(button: OptionsButton, state: OptionState) {
        val newColor = if (state == OptionState.Currect) quizOptionCurrectButtonColor else quizOptionWrongButtonColor
        when (button) {
            OptionsButton.First -> updateState { it.copy(optionsColors = it.optionsColors.mapIndexed { index, color -> if (index == 0) newColor else color }) }
            OptionsButton.Second -> updateState { it.copy(optionsColors = it.optionsColors.mapIndexed { index, color -> if (index == 1) newColor else color }) }
            OptionsButton.Third -> updateState { it.copy(optionsColors = it.optionsColors.mapIndexed { index, color -> if (index == 2) newColor else color }) }
            OptionsButton.Fouth -> updateState { it.copy(optionsColors = it.optionsColors.mapIndexed { index, color -> if (index == 3) newColor else color }) }
        }
    }

    fun onCheckResponse(clickedOptionName: String) {
        viewModelScope.launch {
            dailychallengeUiState.value.dailyQuestion?.let { question ->
                val currectOption = dailychallengeUiState.value.dailyQuestionRightAnswer

                if (quizOptionCurrectButtonColor in dailychallengeUiState.value.optionsColors) return@launch

                val optionsList = question.options
                when (clickedOptionName) {
                    optionsList[0] -> {
                        if (currectOption == clickedOptionName) updateButton(
                            OptionsButton.First,
                            OptionState.Currect
                        )
                        else {
                            updateButton(OptionsButton.First, OptionState.Wrong)
                            when (optionsList.indexOf(currectOption)) {
                                1 -> updateButton(OptionsButton.Second, OptionState.Currect)
                                2 -> updateButton(OptionsButton.Third, OptionState.Currect)
                                3 -> updateButton(OptionsButton.Fouth, OptionState.Currect)
                            }
                        }
                    }

                    optionsList[1] -> {
                        if (currectOption == clickedOptionName) {
                            updateButton(OptionsButton.Second, OptionState.Currect)
                        } else {
                            updateButton(OptionsButton.Second, OptionState.Wrong)
                            when (optionsList.indexOf(currectOption)) {
                                0 -> updateButton(OptionsButton.First, OptionState.Currect)
                                2 -> updateButton(OptionsButton.Third, OptionState.Currect)
                                3 -> updateButton(OptionsButton.Fouth, OptionState.Currect)
                            }
                        }
                    }

                    optionsList[2] -> {
                        if (currectOption == clickedOptionName) {
                            updateButton(OptionsButton.Third, OptionState.Currect)
                        } else {
                            updateButton(OptionsButton.Third, OptionState.Wrong)
                            when (optionsList.indexOf(currectOption)) {
                                0 -> updateButton(OptionsButton.First, OptionState.Currect)
                                1 -> updateButton(OptionsButton.Second, OptionState.Currect)
                                3 -> updateButton(OptionsButton.Fouth, OptionState.Currect)
                            }
                        }
                    }

                    optionsList[3] -> {
                        if (currectOption == clickedOptionName) {
                            updateButton(OptionsButton.Fouth, OptionState.Currect)
                        } else {
                            updateButton(OptionsButton.Fouth, OptionState.Wrong)
                            when (optionsList.indexOf(currectOption)) {
                                0 -> updateButton(OptionsButton.First, OptionState.Currect)
                                1 -> updateButton(OptionsButton.Second, OptionState.Currect)
                                2 -> updateButton(OptionsButton.Third, OptionState.Currect)
                            }
                        }
                    }
                }

                if (currectOption == clickedOptionName) sendSound(SoundEvent.Correct) else sendSound(SoundEvent.Wrong)

            }
        }
    }
}