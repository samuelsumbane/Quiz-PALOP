package com.samuelsumbane.quizpalop.presentation.maingamepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelsumbane.quizpalop.core.HapticManager
import com.samuelsumbane.quizpalop.domain.model.AdState
import com.samuelsumbane.quizpalop.domain.model.Category
import com.samuelsumbane.quizpalop.domain.model.Countries
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class MainGameViewModel(
    private val repo: QuizRepository,
    val settingsManager: SettingsManager,
    val hapticManager: HapticManager
) : ViewModel() {

    private val _state = MutableStateFlow(MainGameUiState())
    val mainGameUiState = _state.asStateFlow()
    private val _soundEvent = Channel<SoundEvent>()
    val soundEvent = _soundEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            loadPacks()
            loadDateTimeSavedValues()
//        loadQuestions()
            loadLivesAndCoinsInFlow()
            settingsManager.readBooleanValue(settingsManager.playSound).collect { savedValue ->
                val soundState = if (savedValue) SoundState.Playing else SoundState.Mute
                updateState { it.copy(
                    soundState = soundState,
                    mobileVibrate = hapticManager.mobileVibrate
                ) }
            }
        }
    }

    fun updateState(block: (MainGameUiState) -> MainGameUiState) = _state.update(block)
    internal fun sendSound(sound: SoundEvent) = viewModelScope.launch { _soundEvent.send(sound) }

    fun onEvent(event: MainGameUiEvents) {
        when (event) {
            is MainGameUiEvents.OnCheckResponse -> checkResponse(event.clickedOptionName)
            MainGameUiEvents.OnExit -> exitGame()
            is MainGameUiEvents.OnHelp -> {
                when (event.helpOption) {
                    HelpOption.FiftFift -> helpWithFiftFift()
                    HelpOption.RightOption -> helpWithRightOption()
                }
            }
            MainGameUiEvents.OnToggleShowConfig -> toogleShowConfigs()
            is MainGameUiEvents.OnToggleSoundState -> toogleSoundState(event.playSound)
            MainGameUiEvents.OnToggleHapticState -> toggleHapticState()
        }
    }

    fun loadPacks() {
        val packs = repo.getPacks()
        updateState { it.copy(packs = packs) }
    }

    fun changeTimerState(newTimerState: QuestionTimerState) = _state.update { it.copy(timerState = newTimerState) }


    fun loadQuestions(country: Countries, category: Category) {
        viewModelScope.launch {
            val savedQuestionsId = settingsManager.readSavedStringsValues(settingsManager.savedQuestionsList).first()
            val questions = repo.getQuestions()
            val selectedQuestionsList = questions.filter { it.id.startsWith(country.code) && it.questionLevel == category.categoryMeaning }
            val idList = selectedQuestionsList.map { it.id }.toSet() - savedQuestionsId
            updateState {
                it.copy(
                    allQuestions = questions,
                    answeredQuestionsList = savedQuestionsId,
                    questionsIdList = idList,
                    selectedCountry = country,
                    selectedCategory = category
                )
            }
            startLoadingNextQuestion()
        }
    }


    fun startLoadingNextQuestion() {
        viewModelScope.launch {
            if (mainGameUiState.value.lives > 0 || mainGameUiState.value.gameTextMessage == GameTextMessage.Empty) {
                delay(1700.milliseconds)
                loadNextQuestion()
            }
        }
    }


    fun loadNextQuestion() {
        println("questions: onLoad, cou: ${mainGameUiState.value.selectedCountry} ${mainGameUiState.value.questionsIdList}")
        if (mainGameUiState.value.questionsIdList.isEmpty()) {
            updateState { it.copy(pageState = MainPageState.QuestionsAnswered) }

            val allAnsweredQuestions = mainGameUiState.value.answeredQuestionsList.size
            if (mainGameUiState.value.questionsIdList.size == allAnsweredQuestions) {
                setGameTextMessage(GameTextMessage.AllQuestionsAnswered("Parabéns!", "Respondeu todas as questões do jogo."))
            } else {
                setGameTextMessage(GameTextMessage.SelectedQuestionsAnswered("Parabéns!", """Respondeu todas as perguntas do país "${mainGameUiState.value.selectedCountry?.countryName}" e categoria "${mainGameUiState.value.selectedCategory?.categoryName}".""", "Deseja limpar e responder mesmas questões ou selecionar outras?"))
            }
        } else {
            setGameTextMessage(GameTextMessage.Empty)
            val randomedQuestionId = mainGameUiState.value.questionsIdList.random()
            mainGameUiState.value.allQuestions.firstOrNull { it.id == randomedQuestionId }?.let { randomedQuestion ->
                val readyQuestion =
                    randomedQuestion.copy(options = randomedQuestion.options.shuffled())
                updateState {
                    it.copy(
                        actualQuestion = readyQuestion,
                        actualQuestionRightAnswer = randomedQuestion.options[randomedQuestion.correctIndex],
                        questionsTimer = 30,
                        pageState = MainPageState.DisplayContent
                    )
                }
                changeTimerState(QuestionTimerState.Running)
                resetOptionsColors()
            }
        }
    }

    fun resetOptionsColors() {
        updateState { it.copy(optionsColors = listOf(quizOptionDefaultColor, quizOptionDefaultColor, quizOptionDefaultColor, quizOptionDefaultColor),) }
    }

    fun loadDateTimeSavedValues() {
        viewModelScope.launch {
            val lastDateTimeUserLostLives = settingsManager.readLongValues(settingsManager.lastDateTimeLostLives)
            val lastDateTimeUserAskedRightOption = settingsManager.readLongValues(settingsManager.lastRightOptionButtonDateTime)

            updateState { it.copy(
                lastDateTimeLostLives = lastDateTimeUserLostLives,
                lastRightOptionButtonDateTime = lastDateTimeUserAskedRightOption
            ) }
        }
    }

    fun loadLivesAndCoinsInFlow() {
        viewModelScope.launch {
            settingsManager.readIntValues(settingsManager.lives, 10).collect { userLives ->
                updateState { it.copy(lives = userLives) }
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

    fun toogleSoundState(playSound: Boolean) {
        viewModelScope.launch {
            settingsManager.saveBooleanValues(settingsManager.playSound, playSound)
            if (mainGameUiState.value.soundState == SoundState.Playing) {
                _soundEvent.send(SoundEvent.Click)
            }
            updateState { it.copy(
                soundState = if (playSound) SoundState.Playing else SoundState.Mute,
                showGameConfigs = false
            ) }
        }
    }

    fun toggleHapticState() {
        viewModelScope.launch {
            settingsManager.saveBooleanValues(settingsManager.vibrateOnTap, !mainGameUiState.value.mobileVibrate)
            updateState {
               it.copy(
                   mobileVibrate = !mainGameUiState.value.mobileVibrate,
                   showGameConfigs = false
               )
            }
        }
    }

    fun toogleShowConfigs() {
        updateState { it.copy(showGameConfigs = !mainGameUiState.value.showGameConfigs) }
    }

    fun changeTimerTo(timer: Int) = updateState { it.copy(questionsTimer = timer,) }


    suspend fun timerCounterExec() {
        if (mainGameUiState.value.timerState == QuestionTimerState.Running && mainGameUiState.value.lives > 0) {
//            viewModelScope.launch {
            val ft = mainGameUiState.value.questionsTimer
            for (i in ft downTo 0) {
                changeTimerTo(i)
                delay(1000.milliseconds)
            }
            if (mainGameUiState.value.questionsTimer <= 1) {
                setQuestionWrong()
                changeTimerState(QuestionTimerState.Stop)
            }
//            }
        }
    }

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
//            println("ouvindo: after question load, sav ${mainGameUiState.value.questionsIdList} $preQuestionsIdList w s: ${savedQuestions}")
//        }
//    }

    fun onSelectNewQuestionsGroup() {
        updateState { it.copy(loadSavedQuestionsFineshed = false) }
        setGameTextMessage(GameTextMessage.Empty)
    }
}