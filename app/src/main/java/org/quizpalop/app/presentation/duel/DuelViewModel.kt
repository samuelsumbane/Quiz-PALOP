package org.quizpalop.app.presentation.duel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.quizpalop.app.core.HapticManager
import org.quizpalop.app.domain.model.Category
import org.quizpalop.app.domain.model.Countries
import org.quizpalop.app.domain.model.Question
import org.quizpalop.app.domain.model.SoundEvent
import org.quizpalop.app.domain.repository.QuizRepository
import org.quizpalop.app.domain.repository.UserPreferencesRepository
import org.quizpalop.app.presentation.maingamepage.OptionState
import org.quizpalop.app.presentation.maingamepage.OptionsButton
import org.quizpalop.app.presentation.maingamepage.quizOptionCorrectButtonColor
import org.quizpalop.app.presentation.maingamepage.quizOptionWrongButtonColor
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class DuelViewModel(
    val repository: QuizRepository,
    val hapticManager: HapticManager,
    val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val _state = MutableStateFlow(DuelUiState())
    val duelUiState = _state.asStateFlow()
    private var countDownJob: Job? = null
    private val _soundEvent = Channel<SoundEvent>()
    val soundEvent = _soundEvent.receiveAsFlow()

    init {
        resetUiState()
        loadVibrateState()
    }

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
                    questionsIdList = questionsForFirstPlayer.map { question -> question.id }
                        .toSet(),
                ),
                secondPlayer = PlayerData(
                    name = PlayerName.SecondPlayer,
                    questionsIdList = questionsForSecondPlayer.map { question -> question.id }
                        .toSet()
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
        if (duelUiState.value.firstPlayer.questionsIdList.isNotEmpty() && duelUiState.value.secondPlayer.questionsIdList.isNotEmpty()) {
            val firstPlayerQuestionId = duelUiState.value.firstPlayer.questionsIdList.random()
            val secondPlayerQuestionId = duelUiState.value.secondPlayer.questionsIdList.random()

            duelUiState.value.allQuestions.firstOrNull { question -> question.id == firstPlayerQuestionId }
                ?.let { foundQuestion ->
                    updateState {
                        it.copy(
                            firstPlayer = it.firstPlayer.copy(
                                question = foundQuestion.copy(options = foundQuestion.options.shuffled()),
                                actualQuestionRightAnswer = foundQuestion.options[foundQuestion.correctIndex]
                            )
                        )
                    }
                }

            duelUiState.value.allQuestions.firstOrNull { secondQuestion -> secondQuestion.id == secondPlayerQuestionId }
                ?.let { questionForSecondPlayer ->
                    updateState {
                        it.copy(
                            secondPlayer = it.secondPlayer.copy(
                                question = questionForSecondPlayer.copy(options = questionForSecondPlayer.options.shuffled()),
                                actualQuestionRightAnswer = questionForSecondPlayer.options[questionForSecondPlayer.correctIndex]
                            ),
                            pageState = PageState.ShowContent
                        )
                    }
                }
            decreaseTimer()
        } else {
            updateState { it.copy(pageState = PageState.DisplayMessage) }
        }
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
                            if (rightOption == clickedOptionName) updateButton(OptionsButton.First, actualPlayerData, OptionState.Correct)
                            else {
                                updateButton(OptionsButton.First, actualPlayerData, OptionState.Wrong)
                                when (optionsList.indexOf(rightOption)) {
                                    1 -> updateButton(OptionsButton.Second, actualPlayerData, OptionState.Correct)
                                    2 -> updateButton(OptionsButton.Third, actualPlayerData, OptionState.Correct)
                                    3 -> updateButton(OptionsButton.Fourth, actualPlayerData, OptionState.Correct)
                                }
                            }
                        }

                        optionsList[1] -> {
                            if (rightOption == clickedOptionName) {
                                updateButton(OptionsButton.Second, actualPlayerData, OptionState.Correct)
                            } else {
                                updateButton(OptionsButton.Second, actualPlayerData, OptionState.Wrong)
                                when (optionsList.indexOf(rightOption)) {
                                    0 -> updateButton(OptionsButton.First, actualPlayerData,  OptionState.Correct)
                                    2 -> updateButton(OptionsButton.Third, actualPlayerData,  OptionState.Correct)
                                    3 -> updateButton(OptionsButton.Fourth, actualPlayerData, OptionState.Correct)
                                }
                            }
                        }

                        optionsList[2] -> {
                            if (rightOption == clickedOptionName) {
                                updateButton(OptionsButton.Third, actualPlayerData, OptionState.Correct)
                            } else {
                                updateButton(OptionsButton.Third, actualPlayerData, OptionState.Wrong)
                                when (optionsList.indexOf(rightOption)) {
                                    0 -> updateButton(OptionsButton.First, actualPlayerData,  OptionState.Correct)
                                    1 -> updateButton(OptionsButton.Second, actualPlayerData,  OptionState.Correct)
                                    3 -> updateButton(OptionsButton.Fourth, actualPlayerData, OptionState.Correct)
                                }
                            }
                        }

                        optionsList[3] -> {
                            if (rightOption== clickedOptionName) {
                                updateButton(OptionsButton.Fourth, actualPlayerData,  OptionState.Correct)
                            } else {
                                updateButton(OptionsButton.Fourth, actualPlayerData,  OptionState.Wrong)
                                when (optionsList.indexOf(rightOption)) {
                                    0 -> updateButton(OptionsButton.First, actualPlayerData,  OptionState.Correct)
                                    1 -> updateButton(OptionsButton.Second, actualPlayerData,  OptionState.Correct)
                                    2 -> updateButton(OptionsButton.Third, actualPlayerData,  OptionState.Correct)
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
                        vibrateOnSuccess()
                        sendSound(SoundEvent.Correct)
                    } else {
                        vibrateOnError()
                        sendSound(SoundEvent.Wrong)
                    }
println("duelS: the id: ${question.id}")
                    delay(1200.milliseconds)
                    if (playerName == PlayerName.FirstPlayer) {
                        val firstOptionsList = duelUiState.value.firstPlayer.questionsIdList - question.id
                        updateState {
                            it.copy(
                                firstPlayer = it.firstPlayer.copy(
                                    question = null,
                                    questionsIdList = firstOptionsList
                                )
                            )
                        }
//                        println("duelS: 1 the list ${duelUiState.value.firstPlayer.questionsIdList} but it is: ${firstOptionsList}")
                    } else {
                        val newSecondOptionsList = duelUiState.value.secondPlayer.questionsIdList - question.id
                        updateState {
                            it.copy(
                                secondPlayer = it.secondPlayer.copy(
                                    question = null,
                                    questionsIdList = newSecondOptionsList
                                )
                            )
                        }
                    println("duelS: 2 the list ${duelUiState.value.secondPlayer.questionsIdList} but it is: ${newSecondOptionsList}")
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
            if (duelUiState.value.firstPlayer.questionsIdList.isNotEmpty()) {
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
                    questionsIdList = questions.shuffled().take(duelUiState.value.duelQuestionsSize).map { question -> question.id }.toSet(),
                    rightAnsweredQuestions = 0
                ),
                secondPlayer = it.secondPlayer.copy(
                    questionsIdList = questions.shuffled().take(duelUiState.value.duelQuestionsSize).map { question -> question.id }.toSet(),
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
                delay(1000.milliseconds)
            }
            updateState { it.copy(
                firstPlayer = it.firstPlayer.copy(question = null, playerTimer = 0),
                secondPlayer = it.secondPlayer.copy(question = null, playerTimer = 0)
            )}

            loadNextQuestionOrShowMessage()
        }
    }

    fun updateButton(button: OptionsButton, playerData: PlayerData, state: OptionState) {
        val newColor = if (state == OptionState.Correct) quizOptionCorrectButtonColor else quizOptionWrongButtonColor

        when (playerData.name) {
            PlayerName.FirstPlayer -> {
                when (button) {
                    OptionsButton.First -> updateState {
                        it.copy(firstPlayer = it.firstPlayer.copy(optionsColors = it.firstPlayer.optionsColors.mapIndexed { index, color -> if (index == 0) newColor else color }))
                    }
                    OptionsButton.Second -> updateState {
                        it.copy(firstPlayer = it.firstPlayer.copy(optionsColors = it.firstPlayer.optionsColors.mapIndexed { index, color -> if (index == 1) newColor else color }))
                    }
                    OptionsButton.Third -> updateState {
                        it.copy(firstPlayer = it.firstPlayer.copy(optionsColors = it.firstPlayer.optionsColors.mapIndexed { index, color -> if (index == 2) newColor else color }))
                    }
                    OptionsButton.Fourth -> updateState {
                        it.copy(firstPlayer = it.firstPlayer.copy(optionsColors = it.firstPlayer.optionsColors.mapIndexed { index, color -> if (index == 3) newColor else color }))
                    }
                }
            }
            PlayerName.SecondPlayer -> {
                when (button) {
                    OptionsButton.First -> updateState {
                        it.copy(secondPlayer = it.secondPlayer.copy(optionsColors = it.secondPlayer.optionsColors.mapIndexed { index, color -> if (index == 0) newColor else color }))
                    }
                    OptionsButton.Second -> updateState {
                        it.copy(secondPlayer = it.secondPlayer.copy(optionsColors = it.secondPlayer.optionsColors.mapIndexed { index, color -> if (index == 1) newColor else color }))
                    }
                    OptionsButton.Third -> updateState {
                        it.copy(secondPlayer = it.secondPlayer.copy(optionsColors = it.secondPlayer.optionsColors.mapIndexed { index, color -> if (index == 2) newColor else color }))
                    }
                    OptionsButton.Fourth -> updateState {
                        it.copy(secondPlayer = it.secondPlayer.copy(optionsColors = it.secondPlayer.optionsColors.mapIndexed { index, color -> if (index == 3) newColor else color }))
                    }
                }
            }
        }
    }

    fun loadVibrateState() {
        viewModelScope.launch {
            val vibrateOnTap = userPreferencesRepository.loadVibrateOnTap()
            updateState { it.copy(mobileVibrate = vibrateOnTap) }
        }
    }

    fun vibrateOnSuccess() {
        if (duelUiState.value.mobileVibrate) hapticManager.success()
    }

    fun vibrateOnError() {
        if (duelUiState.value.mobileVibrate) hapticManager.error()
    }
}




