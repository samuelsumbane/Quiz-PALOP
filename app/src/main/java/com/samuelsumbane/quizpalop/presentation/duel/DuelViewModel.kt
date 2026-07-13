package com.samuelsumbane.quizpalop.presentation.duel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelsumbane.quizpalop.domain.model.Category
import com.samuelsumbane.quizpalop.domain.model.Countries
import com.samuelsumbane.quizpalop.domain.model.Question
import com.samuelsumbane.quizpalop.domain.model.SoundEvent
import com.samuelsumbane.quizpalop.domain.repository.QuizRepository
import com.samuelsumbane.quizpalop.presentation.maingamepage.OptionState
import com.samuelsumbane.quizpalop.presentation.maingamepage.OptionsButton
import com.samuelsumbane.quizpalop.presentation.maingamepage.quizOptionCurrectButtonColor
import com.samuelsumbane.quizpalop.presentation.maingamepage.quizOptionWrongButtonColor
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DuelViewModel(val repository: QuizRepository) : ViewModel() {
    private val _state = MutableStateFlow(DuelUiState())
    val duelUiState = _state.asStateFlow()
    private var countDownJob: Job? = null
//    private val duelQuestionsSize = 20
    private val _soundEvent = Channel<SoundEvent>()
    val soundEvent = _soundEvent.receiveAsFlow()


    init { resetUiState() }

    fun onEvent(duelUiEvents: DuelUiEvents) {
        when (duelUiEvents) {
            is DuelUiEvents.OnCheckPlayerResponse -> checkResponse(duelUiEvents.playerData, duelUiEvents.clickedOptionName)
            DuelUiEvents.OnLoadNewDuelWithSameCategoryAndLevel -> loadNewDuelWithSavedCategoryAndLevel()
        }
    }

    internal fun sendSound(sound: SoundEvent) = viewModelScope.launch { _soundEvent.send(sound) }


    fun updateState(block: (DuelUiState) -> DuelUiState) = _state.update(block)

    fun loadData(country: Countries, category: Category, duelQuestionsSize: Int) {
        viewModelScope.launch {
            val allQuestions = repository.getQuestions()
            val filteredQuestions = allQuestions
                .filter { it.id.startsWith(country.code) && it.questionLevel == category.categoryMeaning }
            val questionsForFirstPlayer = filteredQuestions.shuffled().take(duelQuestionsSize).toSet()
            val questionsForSecondPlayer = filteredQuestions.shuffled().take(duelQuestionsSize).toSet()
            updateState { it.copy(
                allQuestions = filteredQuestions,
                firstPlayer = PlayerData(
                    name = PlayerName.FirstPlayer,
                    questionsList = questionsForFirstPlayer,
                ),
                secondPlayer = PlayerData(
                    name = PlayerName.SecondPlayer,
                    questionsList = questionsForSecondPlayer
                ),
                country = country,
                category = category,
                pageState = PageState.Loading,
                duelQuestionsSize = duelQuestionsSize
            ) }
        }
    }

    fun resetUiState() {
        _state.value = DuelUiState()
    }

    fun loadNextQuestionForBothPlayers() {
        updateState {
            val firstPlayerQuestion = duelUiState.value.firstPlayer.questionsList.random()
            val secondPlayerQuestion = duelUiState.value.secondPlayer.questionsList.random()
            
            it.copy(
                firstPlayer = it.firstPlayer.copy(
                    question = firstPlayerQuestion.copy(options = firstPlayerQuestion.options.shuffled()),
                    actualQuestionRightAnswer = firstPlayerQuestion.options[firstPlayerQuestion.correctIndex]
                ),
                secondPlayer = it.secondPlayer.copy(
                    question = secondPlayerQuestion.copy(options = secondPlayerQuestion.options.shuffled()),
                    actualQuestionRightAnswer = secondPlayerQuestion.options[secondPlayerQuestion.correctIndex]
                ),
                pageState = PageState.ShowContent
            )
        }
        decreaseTimer()
    }


    fun checkResponse(playerData: PlayerData, clickedOptionName: String) {

        fun checkingResponse (playerName: PlayerName, question: Question, clickedOptionName: String) {
            val actualPlayerData =
                if (playerData.name == PlayerName.FirstPlayer) duelUiState.value.firstPlayer
                else duelUiState.value.secondPlayer
            val optionsList = actualPlayerData.question?.options

            optionsList?.let {
                val rightOption = actualPlayerData.actualQuestionRightAnswer

                viewModelScope.launch {
                    when (clickedOptionName) {
                        optionsList[0] -> {
                            if (rightOption == clickedOptionName) updateButton(OptionsButton.First, actualPlayerData, OptionState.Currect)
                            else {
                                updateButton(OptionsButton.First, actualPlayerData, OptionState.Wrong)
                                when (optionsList.indexOf(rightOption)) {
                                    1 -> updateButton(OptionsButton.Second, actualPlayerData, OptionState.Currect)
                                    2 -> updateButton(OptionsButton.Third, actualPlayerData, OptionState.Currect)
                                    3 -> updateButton(OptionsButton.Fouth, actualPlayerData, OptionState.Currect)
                                }
                            }
                        }

                        optionsList[1] -> {
                            if (rightOption == clickedOptionName) {
                                updateButton(OptionsButton.Second, actualPlayerData, OptionState.Currect)
                            } else {
                                updateButton(OptionsButton.Second, actualPlayerData, OptionState.Wrong)
                                when (optionsList.indexOf(rightOption)) {
                                    0 -> updateButton(OptionsButton.First, actualPlayerData,  OptionState.Currect)
                                    2 -> updateButton(OptionsButton.Third, actualPlayerData,  OptionState.Currect)
                                    3 -> updateButton(OptionsButton.Fouth, actualPlayerData, OptionState.Currect)
                                }
                            }
                        }

                        optionsList[2] -> {
                            if (rightOption == clickedOptionName) {
                                updateButton(OptionsButton.Third, actualPlayerData, OptionState.Currect)
                            } else {
                                updateButton(OptionsButton.Third, actualPlayerData, OptionState.Wrong)
                                when (optionsList.indexOf(rightOption)) {
                                    0 -> updateButton(OptionsButton.First, actualPlayerData,  OptionState.Currect)
                                    1 -> updateButton(OptionsButton.Second, actualPlayerData,  OptionState.Currect)
                                    3 -> updateButton(OptionsButton.Fouth, actualPlayerData, OptionState.Currect)
                                }
                            }
                        }

                        optionsList[3] -> {
                            if (rightOption== clickedOptionName) {
                                updateButton(OptionsButton.Fouth, actualPlayerData,  OptionState.Currect)
                            } else {
                                updateButton(OptionsButton.Fouth, actualPlayerData,  OptionState.Wrong)
                                when (optionsList.indexOf(rightOption)) {
                                    0 -> updateButton(OptionsButton.First, actualPlayerData,  OptionState.Currect)
                                    1 -> updateButton(OptionsButton.Second, actualPlayerData,  OptionState.Currect)
                                    2 -> updateButton(OptionsButton.Third, actualPlayerData,  OptionState.Currect)
                                }
                            }
                        }
                    }

                    if (rightOption == clickedOptionName) {
                        if (playerName == PlayerName.FirstPlayer) {
                            updateState {
                                it.copy(
                                    firstPlayer = it.firstPlayer.copy(rightAnsweredQuestions = it.firstPlayer.rightAnsweredQuestions + 1)
                                )
                            }
                        } else {
                            updateState {
                                it.copy(
                                    secondPlayer = it.secondPlayer.copy(rightAnsweredQuestions = it.secondPlayer.rightAnsweredQuestions + 1)
                                )
                            }
                        }
                        sendSound(SoundEvent.Correct)
                    } else sendSound(SoundEvent.Wrong)

                    delay(1200)

                    if (playerName == PlayerName.FirstPlayer) {
                        updateState {
                            it.copy(
                                firstPlayer = it.firstPlayer.copy(
                                    question = null,
                                    questionsList = it.firstPlayer.questionsList - question
                                )
                            )
                        }
                    } else {
                        updateState {
                            it.copy(
                                secondPlayer = it.secondPlayer.copy(
                                    question = null,
                                    questionsList = it.secondPlayer.questionsList - question
                                )
                            )
                        }
                    }

                    loadNextQuestionOrShowMessage()
                }
            }
        }

        playerData.let {
            it.question?.let { question -> checkingResponse(it.name, question, clickedOptionName) }
        }
    }

    fun loadNextQuestionOrShowMessage() {
        if (duelUiState.value.firstPlayer.question == null && duelUiState.value.secondPlayer.question == null) {
            resetButtonsColors()
            if (duelUiState.value.firstPlayer.questionsList.isNotEmpty()) {
                loadNextQuestionForBothPlayers()
            } else {
                updateState { it.copy(pageState = PageState.DisplayMessage) }
            }
        }
    }
    fun loadNewDuelWithSavedCategoryAndLevel() {
        val questions = duelUiState.value.allQuestions.filter { it.id.startsWith(duelUiState.value.country.code) && it.questionLevel == duelUiState.value.category.categoryMeaning }

        updateState {
            it.copy(
                firstPlayer = it.firstPlayer.copy(
                    questionsList = questions.shuffled().take(duelUiState.value.duelQuestionsSize).toSet(),
                    rightAnsweredQuestions = 0
                ),
                secondPlayer = it.secondPlayer.copy(
                    questionsList = questions.shuffled().take(duelUiState.value.duelQuestionsSize).toSet(),
                    rightAnsweredQuestions = 0
                )
            )
        }
        loadNextQuestionForBothPlayers()
    }

    fun decreaseTimer() {
        countDownJob?.cancel()

        countDownJob = viewModelScope.launch {
            for (i in 60 downTo 0) {
                if (duelUiState.value.firstPlayer.question != null) {
                    if (i > 0) {
                        updateState { it.copy(firstPlayer = it.firstPlayer.copy(playerTimer = i)) }
                    } else {
                        // Give second player a point
                       if (duelUiState.value.secondPlayer.question == null) {
                           updateState { it.copy(secondPlayer = it.secondPlayer.copy(rightAnsweredQuestions = it.secondPlayer.rightAnsweredQuestions + 1)) }
                       }

                    }
                }

                if (duelUiState.value.secondPlayer.question != null) {
                    if (i > 0) {
                        updateState { it.copy(secondPlayer = it.secondPlayer.copy(playerTimer = i)) }
                    } else {
                        // Give first player a point
                        if (duelUiState.value.firstPlayer.question == null) {
                            updateState { it.copy(firstPlayer = it.firstPlayer.copy(rightAnsweredQuestions = it.firstPlayer.rightAnsweredQuestions + 1)) }
                        }

                    }
                }
                if (duelUiState.value.firstPlayer.question == null && duelUiState.value.secondPlayer.question == null) {
                    break
                }
                delay(1000)
            }
            updateState { it.copy(
                firstPlayer = it.firstPlayer.copy(question = null, playerTimer = 0),
                secondPlayer = it.secondPlayer.copy(question = null, playerTimer = 0)
            )}

            loadNextQuestionOrShowMessage()
        }
    }

    fun updateButton(button: OptionsButton, playerData: PlayerData, state: OptionState) {
        val newColor = if (state == OptionState.Currect) quizOptionCurrectButtonColor else quizOptionWrongButtonColor

        when (playerData.name) {
            PlayerName.FirstPlayer -> {
                when (button) {
                    OptionsButton.First -> updateState {
                        it.copy(firstPlayer = it.firstPlayer.copy(optionsColors = it.firstPlayer.optionsColors.mapIndexed { index, color -> if (index == 0) newColor else color }),)
                    }
                    OptionsButton.Second -> updateState {
                        it.copy(firstPlayer = it.firstPlayer.copy(optionsColors = it.firstPlayer.optionsColors.mapIndexed { index, color -> if (index == 1) newColor else color }))
                    }
                    OptionsButton.Third -> updateState {
                        it.copy(firstPlayer = it.firstPlayer.copy(optionsColors = it.firstPlayer.optionsColors.mapIndexed { index, color -> if (index == 2) newColor else color }))
                    }
                    OptionsButton.Fouth -> updateState {
                        it.copy(firstPlayer = it.firstPlayer.copy(optionsColors = it.firstPlayer.optionsColors.mapIndexed { index, color -> if (index == 3) newColor else color }))
                    }
                }
            }
            PlayerName.SecondPlayer -> {
                when (button) {
                    OptionsButton.First -> updateState {
                        it.copy(secondPlayer = it.secondPlayer.copy(optionsColors = it.secondPlayer.optionsColors.mapIndexed { index, color -> if (index == 0) newColor else color }),)
                    }
                    OptionsButton.Second -> updateState {
                        it.copy(secondPlayer = it.secondPlayer.copy(optionsColors = it.secondPlayer.optionsColors.mapIndexed { index, color -> if (index == 1) newColor else color }))
                    }
                    OptionsButton.Third -> updateState {
                        it.copy(secondPlayer = it.secondPlayer.copy(optionsColors = it.secondPlayer.optionsColors.mapIndexed { index, color -> if (index == 2) newColor else color }))
                    }
                    OptionsButton.Fouth -> updateState {
                        it.copy(secondPlayer = it.secondPlayer.copy(optionsColors = it.secondPlayer.optionsColors.mapIndexed { index, color -> if (index == 3) newColor else color }))
                    }
                }
            }
        }


    }

}




