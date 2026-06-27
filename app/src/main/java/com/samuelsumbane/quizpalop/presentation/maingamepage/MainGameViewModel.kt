package com.samuelsumbane.quizpalop.presentation.maingamepage

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelsumbane.quizpalop.domain.model.AdState
import com.samuelsumbane.quizpalop.domain.model.HelpOption
import com.samuelsumbane.quizpalop.domain.model.QuestionTimerState
import com.samuelsumbane.quizpalop.domain.model.SoundEvent
import com.samuelsumbane.quizpalop.domain.repository.QuizRepository
import com.samuelsumbane.quizpalop.domain.repository.RewardedAdManager
import com.samuelsumbane.quizpalop.domain.repository.SettingsManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainGameViewModel(
    private val repo: QuizRepository,
    val settingsManager: SettingsManager
) : ViewModel() {

    private val _state = MutableStateFlow(MainGameUiState())
    val mainGameUiState = _state.asStateFlow()
    private val _soundEvent = Channel<SoundEvent>()
    val soundEvent = _soundEvent.receiveAsFlow()

    init {
        loadPacks()
        loadQuestions()
    }

    fun updateState(block: (MainGameUiState) -> MainGameUiState) = _state.update(block)
    internal fun sendSound(sound: SoundEvent) = viewModelScope.launch { _soundEvent.send(sound) }


    fun onEvent(event: MainGameUiEvents) {
        when (event) {
            is MainGameUiEvents.OnCheckResponse -> checkResponse(event.clickedOptionName)
            is MainGameUiEvents.OnExit -> exitGame()
            is MainGameUiEvents.OnHelp -> {
                when (event.helpOption) {
                    HelpOption.FiftFift -> helpWithFiftFift()
                    HelpOption.RightOption -> helpWithRightOption()
                }
            }
        }
    }

    fun loadPacks() {
        val packs = repo.getPacks()
        updateState { it.copy(packs = packs) }
    }

    fun changeTimerState(newTimerState: QuestionTimerState) = _state.update { it.copy(timerState = newTimerState) }


    fun loadQuestions() {
        viewModelScope.launch {
            val questions = repo.getQuestions()
            updateState {
                it.copy(
                    questions = questions,
                    pageState = MainPageState.DisplayContent
                )
            }
        }
        loadNextQuestion()
    }



    fun loadNextQuestion() {
        viewModelScope.launch {
            val randomedQuestion = mainGameUiState.value.questions.random()
            val readyQuestion = randomedQuestion.copy(options = randomedQuestion.options.shuffled())
            delay(1700)
            updateState {
                it.copy(
                    actualQuestion = readyQuestion,
                    actualQuestionRightAnswer = randomedQuestion.options[randomedQuestion.correctIndex]
                )
            }
            resetOptionsColors()
        }
    }

    fun resetOptionsColors() {
        updateState { it.copy(optionsColors = listOf(quizOptionDefaultColor, quizOptionDefaultColor, quizOptionDefaultColor, quizOptionDefaultColor),) }
    }

    fun loadLivesAndCoinsInFlow() {
        viewModelScope.launch {
            settingsManager.readIntValues(settingsManager.lives).collect { lives ->
                updateState {
                    it.copy(lives = lives)
                }
            }
        }

        viewModelScope.launch {
            settingsManager.readIntValues(settingsManager.userCoins).collect { coins ->
                updateState { it.copy(userCoins = coins) }
            }
        }
    }

    fun setAdSate(adState: AdState) = updateState { it.copy(adState = adState) }

    fun loadAd(manager: RewardedAdManager) {
        manager.loadAd { setAdSate(AdState.Ready) }
    }

//    fun loadLivesAndCoinsOnTime() {
//        viewModelScope.launch {
//            val lives = settingsManager.readIntValues(settingsManager.lives).first()
//            val coins = settingsManager.readIntValues(settingsManager.userCoins).first()
//            val lastDateTimeUserLostLives = settingsManager.readLongValues(settingsManager.lastDateTimeLostLives)
//            updateState {
//                it.copy(lives = lives, userCoins = coins, lastDateTimeLostLives = lastDateTimeUserLostLives)
//            }
//        }
//    }

//    fun fillSavedQuestions(questionsCategory: QuestionCategory, questionsLevel: QuestionLevel) {
//        viewModelScope.launch {
//            val preQuestionsIdList = getCategoryAndLevelIds(questionsCategory, questionsLevel)
//            val savedQuestions = settingsManager.readSavedQuestionsList().first()
//            println("ouvindo: pre ${preQuestionsIdList.sorted()}")
//            println("ouvindo: saved ${savedQuestions.sorted()}")
//            updateState { it.copy(
//                questionsIdList = preQuestionsIdList - savedQuestions,
//                answeredQuestionsList = savedQuestions,
//                loadSavedQuestionsFineshed = true)
//            }
//            println("ouvindo: after question load, sav ${quizGameUiState.value.questionsIdList} $preQuestionsIdList w s: ${savedQuestions}")
//        }
//    }

//    fun loadRightAnswerButtonLastClicked() {
//        viewModelScope.launch {
//            val lastRightOptionButtonDateTime = settingsManager.readLongValues(settingsManager.lastRightOptionButtonDateTime)
//            updateState { it.copy(lastRightOptionButtonDateTime = lastRightOptionButtonDateTime) }
//        }
//    }

    private fun MainGameViewModel.exitGame() {
        setGameTextMessage(GameTextMessage.ExitGame("Tem certeza que deseja sair?"))
    }

}