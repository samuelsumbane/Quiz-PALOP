package com.samuelsumbane.quizpalop.presentation.maingamepage

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


enum class OptionsButton { First, Second, Third, Fouth }
enum class OptionState { Currect, Wrong }

fun MainGameViewModel.updateButton(button: OptionsButton, state: OptionState) {
    val newColor = if (state == OptionState.Currect) quizOptionCurrectButtonColor else quizOptionWrongButtonColor
    when (button) {
        OptionsButton.First -> updateState { it.copy(optionsColors = it.optionsColors.mapIndexed { index, color -> if (index == 0) newColor else color }) }
        OptionsButton.Second -> updateState { it.copy(optionsColors = it.optionsColors.mapIndexed { index, color -> if (index == 1) newColor else color }) }
        OptionsButton.Third -> updateState { it.copy(optionsColors = it.optionsColors.mapIndexed { index, color -> if (index == 2) newColor else color }) }
        OptionsButton.Fouth -> updateState { it.copy(optionsColors = it.optionsColors.mapIndexed { index, color -> if (index == 3) newColor else color }) }
    }
}

fun MainGameViewModel.checkResponse(
    clickedOptionName: String,
) {
    viewModelScope.launch {
        mainGameUiState.value.actualQuestion?.let { question ->
            val currectOption = mainGameUiState.value.actualQuestionRightAnswer

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

            delay(1700)
            loadNextQuestion()

//        updateState { it.copy(timerState = QuestionTimerState.Stop) }

//        if (question.correctOptionId == clickedOptionName) {
//            viewModelScope.launch {
//                updateState { it.copy(questionsIdList = it.questionsIdList - question.id) }
//
//                if (!quizGameUiState.value.userGotHelp) {
//                    updateState {
//                        it.copy(
//                            answeredQuestionsWithoutMistake = it.answeredQuestionsWithoutMistake + 1
//                        )
//                    }
//                    giveCoinsToUser()
//                }
//
//                val answeredQuestions = quizGameUiState.value.answeredQuestionsList + question.id
////                val answeredQuestions = quizGameUiState.value.answeredQuestionsList + Random.nextInt()
//                updateState { it.copy(answeredQuestionsList = answeredQuestions) }
////                println("ouvindo: to save: ${quizGameUiState.value.answeredQuestionsList}")
//                settingsManager.saveQuestionsList(quizGameUiState.value.answeredQuestionsList)
//
//                sendSound(SoundEvent.Correct)
//                startLoadingNextQuestion()
//            }
//        } else {
//            viewModelScope.launch {
//                changeLivesCount(ChangeCountValues.DecreaseLive)
//                clearAnsweredQuestionsWithoutMistake()
//                setLastDateTimeUserLostLives()
//                if (quizGameUiState.value.userCoins == 0) changeTimerState(QuestionTimerState.Stop)
//                sendSound(SoundEvent.Wrong)
//                startLoadingNextQuestion()
//            }
//        }

//        changeUserHelpState(false)
        }
    }
}
