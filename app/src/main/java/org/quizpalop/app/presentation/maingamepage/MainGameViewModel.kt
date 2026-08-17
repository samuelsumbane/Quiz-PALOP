package org.quizpalop.app.presentation.maingamepage

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.quizpalop.app.core.HapticManager
import org.quizpalop.app.core.RewardedAdManager
import org.quizpalop.app.domain.model.AdState
import org.quizpalop.app.domain.model.Category
import org.quizpalop.app.domain.model.Countries
import org.quizpalop.app.domain.model.HelpOption
import org.quizpalop.app.domain.model.QuestionTimerState
import org.quizpalop.app.domain.model.SoundEvent
import org.quizpalop.app.domain.repository.LifeNotificationScheduler
import org.quizpalop.app.domain.repository.QuizRepository
import org.quizpalop.app.domain.repository.UserPreferencesRepository
import org.quizpalop.app.domain.usecase.DecreaseLifeUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.quizpalop.app.domain.repository.QuestionsConfigRepository
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class MainGameViewModel(
    val repo: QuizRepository,
    val questionsConfigRepository: QuestionsConfigRepository,
    private val hapticManager: HapticManager,
    val lifeNotificationScheduler: LifeNotificationScheduler,
    val decreaseLifeUseCase: DecreaseLifeUseCase,
    val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MainGameUiState())
    val mainGameUiState = _state.asStateFlow()
    private val _soundEvent = Channel<SoundEvent>()
    val soundEvent = _soundEvent.receiveAsFlow()
    private val _adState = MutableStateFlow(AdState.Loading)
    val appAdState = _adState.asStateFlow()

    lateinit var rewardedAdManager: RewardedAdManager

    fun initializeAds(context: Context) {
        if (::rewardedAdManager.isInitialized) return
        rewardedAdManager = RewardedAdManager(context = context.applicationContext)
        loadRewardAd()
    }

    init {
        viewModelScope.launch {
            userPreferencesRepository.flowLoadPlayOnTap().collect { savedValue ->
                val soundState = if (savedValue) SoundState.Playing else SoundState.Mute
                updateState { it.copy(soundState = soundState) }
            }
        }
    }

    fun resetUiState() {
        _state.value = MainGameUiState()
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
            MainGameUiEvents.OnToggleShowConfig -> toggleShowConfigs()
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
            val savedQuestionsId = repo.loadSavedQuestionsList()

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
            mainGameUiState.value.lives?.let { userLives ->
                if (userLives > 0 || mainGameUiState.value.gameTextMessage == GameTextMessage.Empty) {
                    delay(1700.milliseconds)
                    loadNextQuestion()
                }
            }
        }
    }

    fun loadNextQuestion() {
        if (mainGameUiState.value.questionsIdList.isEmpty()) {
            updateState { it.copy(pageState = MainPageState.QuestionsAnswered) }

            val allAnsweredQuestions = mainGameUiState.value.answeredQuestionsList.size
            if (mainGameUiState.value.allQuestions.size == allAnsweredQuestions) {
                setGameTextMessage(GameTextMessage.AllQuestionsAnswered("Parabéns!!!", "Respondeu todas as questões do jogo."))
            } else {
                setGameTextMessage(GameTextMessage.SelectedQuestionsAnswered("Parabéns!", """Respondeu todas as perguntas do país "${mainGameUiState.value.selectedCountry?.countryName}" e categoria "${mainGameUiState.value.selectedCategory?.categoryName}".""", "Carregar prox. categoria ou sel. outro país e/ou categoria"))
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
        updateState { it.copy(optionsColors = listOf(
            quizOptionDefaultColor,
            quizOptionDefaultColor,
            quizOptionDefaultColor,
            quizOptionDefaultColor
        ),) }
    }

    fun loadDateTimeSavedValues() {
        viewModelScope.launch {
            val lastDateTimeUserLostLives = repo.loadLastDateTimeUserLostLives()

            val lastDateTimeUserAskedRightOption = repo.loadLastDateTimeUserAskedRightOption()

            updateState { it.copy(
                lastDateTimeLostLives = lastDateTimeUserLostLives,
                lastRightOptionButtonDateTime = lastDateTimeUserAskedRightOption
            ) }
        }
    }

    fun loadLivesAndCoinsInFlow() {
        viewModelScope.launch {
            repo.loadUserLivesFlow().collect { userLives -> updateState { it.copy(lives = userLives) } }
        }

        viewModelScope.launch {
            repo.loadUserCoinsFlow().collect { userCoins -> updateState { it.copy(userCoins = userCoins) } }
        }
    }

    fun setAdSate(adState: AdState) = updateState { it.copy(adState = adState) }

    fun loadAd() {
        rewardedAdManager.loadAd { setAdSate(AdState.Ready) }
    }

    private fun MainGameViewModel.exitGame() {
        setGameTextMessage(GameTextMessage.ExitGame("Tem certeza que deseja sair?"))
    }

    fun toogleSoundState(playSound: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updatePlayOnTap(playSound)
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
            userPreferencesRepository.updateVibrateOnTap(!mainGameUiState.value.mobileVibrate)
            updateState {
               it.copy(
                   mobileVibrate = !mainGameUiState.value.mobileVibrate,
                   showGameConfigs = false
               )
            }
        }
    }

    fun toggleShowConfigs() {
        updateState { it.copy(showGameConfigs = !mainGameUiState.value.showGameConfigs) }
    }

    fun changeTimerTo(timer: Int) = updateState { it.copy(questionsTimer = timer,) }


    suspend fun timerCounterExec() {
        mainGameUiState.value.lives?.let { userLives ->
            if (mainGameUiState.value.timerState == QuestionTimerState.Running && userLives > 0) {
//            viewModelScope.launch {
                val ft = mainGameUiState.value.questionsTimer
                for (i in ft downTo 0) {
                    changeTimerTo(i)
                    delay(1.seconds)
                }
                if (mainGameUiState.value.questionsTimer <= 1) {
                    setQuestionWrong()
                    changeTimerState(QuestionTimerState.Stop)
                }
//            }
            }
        }
    }


    private fun loadRewardAd() {

        rewardedAdManager.loadAd(

            onLoaded = {
                _adState.value = AdState.Ready
                updateState { it.copy(adState = appAdState.value) }
            },

            onFailed = { error ->
                _adState.value = AdState.Error
                updateState { it.copy(adState = appAdState.value) }
                viewModelScope.launch {
                    delay(3.seconds)
                    loadRewardAd()
                }
            }
        )
    }

    fun onSelectNewQuestionsGroup() {
        updateState { it.copy(loadSavedQuestionsFineshed = false) }
        setGameTextMessage(GameTextMessage.Empty)
    }

    fun loadSavedHapticAndPlaySound() {
        viewModelScope.launch {
            val mobileVibrate = userPreferencesRepository.loadVibrateOnTap()
            val playSound = userPreferencesRepository.loadPlayOnTap()
            updateState { it.copy(mobileVibrate = mobileVibrate, soundState = if (playSound) SoundState.Playing else SoundState.Mute) }
        }
    }

    fun vibrateOnSuccess() {
        if (mainGameUiState.value.mobileVibrate) hapticManager.success()
    }

    fun vibrateOnError() {
        if (mainGameUiState.value.mobileVibrate) hapticManager.error()
    }
}
