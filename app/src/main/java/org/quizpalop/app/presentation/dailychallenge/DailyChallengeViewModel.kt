package org.quizpalop.app.presentation.dailychallenge

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.quizpalop.app.core.isDifferentDay
import org.quizpalop.app.core.saveBitmap
import org.quizpalop.app.core.shareImage
import org.quizpalop.app.domain.model.SoundEvent
import org.quizpalop.app.domain.repository.DailyChallengeRepository
import org.quizpalop.app.domain.repository.DailyNotificationScheduler
import org.quizpalop.app.domain.repository.QuizRepository
import org.quizpalop.app.domain.repository.UserPreferencesRepository
import org.quizpalop.app.presentation.composables.PageUiState
import org.quizpalop.app.presentation.maingamepage.OptionState
import org.quizpalop.app.presentation.maingamepage.OptionsButton
import org.quizpalop.app.presentation.maingamepage.SoundState
import org.quizpalop.app.presentation.maingamepage.quizOptionCorrectButtonColor
import org.quizpalop.app.presentation.maingamepage.quizOptionWrongButtonColor
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@RequiresApi(Build.VERSION_CODES.O)
class DailyChallengeViewModel(
    private val repo: QuizRepository,
    private val dailyChallengeRepository: DailyChallengeRepository,
    private val dailyNotificationScheduler: DailyNotificationScheduler,
    private val userPreferencesRepo: UserPreferencesRepository
) : ViewModel() {
    private val _dailyChallengeViewModel = MutableStateFlow(DailyChallengeUiState())
    val dailychallengeUiState = _dailyChallengeViewModel.asStateFlow()
    private val _soundEvent = Channel<SoundEvent>()
    val soundEvent = _soundEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            val playOnTap = userPreferencesRepo.loadPlayOnTap()
            val soundState = if (playOnTap) SoundState.Playing else SoundState.Mute
            updateState { it.copy(soundState = soundState) }
        }
    }
    fun resetState() {
        _dailyChallengeViewModel.value = DailyChallengeUiState()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAllSavedDailyQuestions() {
        viewModelScope.launch {
//            val lastDateUserGotDailyQuestion = 1753254000000L
            val now = System.currentTimeMillis()
            val lastDateUserGotDailyQuestion = dailyChallengeRepository.loadLastDateTimeUserGotDailyQuestionId()

            val actualDailyQuestionId = dailyChallengeRepository.loadActualDailyQuestionId()

            if (isDifferentDay(lastDateUserGotDailyQuestion, now)) {
                val allQuestions = repo.getQuestions()
                val savedDailyQuestions = dailyChallengeRepository.loadSavedDailyQuestions()
                val allQuestionsId = allQuestions.map { it.id }.toSet()

                val allEasyQuestions =
                    allQuestions.filter { it.questionLevel == "Easy" }.map { it.id }
                val allMediumQuestions =
                    allQuestions.filter { it.questionLevel == "Medium" }.map { it.id }
                val allHardQuestions =
                    allQuestions.filter { it.questionLevel == "Hard" }.map { it.id }

                val easySavedQuestions = allEasyQuestions intersect savedDailyQuestions
                val mediumSavedQuestions = allMediumQuestions intersect savedDailyQuestions
                val hardSavedQuestions = allHardQuestions intersect savedDailyQuestions

                val easyQuestionsPercentage = easySavedQuestions.size.toFloat() / allEasyQuestions.size
                val mediumQuestionsPercentage =
                    mediumSavedQuestions.size.toFloat() / allMediumQuestions.size
                val hardQuestionsPercentage =
                    hardSavedQuestions.size.toFloat() / allHardQuestions.size


                val allNotAnsweredQuestions = allQuestionsId subtract savedDailyQuestions
                if (allNotAnsweredQuestions.isEmpty()) {
                    dailyChallengeRepository.saveDailyQuestions(emptySet())
                }

                val availablesQuestionsId = when {
                    easyQuestionsPercentage < 1.0f -> allNotAnsweredQuestions subtract listOf(mediumSavedQuestions, hardSavedQuestions).toSet()
                    mediumQuestionsPercentage < 1.0f -> allNotAnsweredQuestions subtract hardSavedQuestions
                    else -> allNotAnsweredQuestions
                }

                val randomedQuestionId = availablesQuestionsId.random()
                val randomedQuestion =
                    allQuestions.first { it.id == randomedQuestionId }
                updateState {
                    it.copy(
                        dailyQuestionId = randomedQuestion.id,
                        dailyChallengeLoadQuestionState = DailyChallengeLoadQuestionState.FINISHED
                    )
                }
                dailyChallengeRepository.saveActualDailyQuestionId(randomedQuestion.id)
                dailyChallengeRepository.saveLastDateTimeUserGotDailyQuestionId(now)
            } else {
                updateState { it.copy(
                    dailyQuestionId = actualDailyQuestionId.ifBlank { null },
                    dailyChallengeLoadQuestionState = DailyChallengeLoadQuestionState.FINISHED
                ) }
            }
        }
    }

    fun updateState(block: (DailyChallengeUiState) -> DailyChallengeUiState) = _dailyChallengeViewModel.update(block)

    internal fun sendSound(sound: SoundEvent) = viewModelScope.launch { _soundEvent.send(sound) }

    fun onEvent(event: DailyChallengeUiEvents) {
        when (event) {
            is DailyChallengeUiEvents.OnCheckResponse -> onCheckResponse(event.questionOption)
            is DailyChallengeUiEvents.OnPrintScree -> onPrintScreen(event.context, event.graphicsLayer)
            is DailyChallengeUiEvents.OnCloseMessageContainer -> onCloseMessageContainer()
        }
    }
    
    fun loadQuestion(questionId: String) {
        viewModelScope.launch {
            val allQuestions = repo.getQuestions()
            allQuestions.firstOrNull { it.id == questionId }?.let { question ->
                updateState { it.copy(
                    dailyQuestion = question.copy(options = question.options.shuffled()),
                    dailyQuestionRightAnswer = question.options[question.correctIndex],
                    questionCountry = question.getCountry(),
                    questionCategory = question.getCategory(),
                    pageUiState = PageUiState.DisplayContent
                ) }
            }
        }
    }

    fun updateButton(button: OptionsButton, state: OptionState) {
        val newColor = if (state == OptionState.Correct) quizOptionCorrectButtonColor else quizOptionWrongButtonColor
        when (button) {
            OptionsButton.First -> updateState { it.copy(optionsColors = it.optionsColors.mapIndexed { index, color -> if (index == 0) newColor else color }) }
            OptionsButton.Second -> updateState { it.copy(optionsColors = it.optionsColors.mapIndexed { index, color -> if (index == 1) newColor else color }) }
            OptionsButton.Third -> updateState { it.copy(optionsColors = it.optionsColors.mapIndexed { index, color -> if (index == 2) newColor else color }) }
            OptionsButton.Fourth -> updateState { it.copy(optionsColors = it.optionsColors.mapIndexed { index, color -> if (index == 3) newColor else color }) }
        }
    }

    fun onCheckResponse(clickedOptionName: String) {
        viewModelScope.launch {
            dailychallengeUiState.value.dailyQuestion?.let { question ->
                val currectOption = dailychallengeUiState.value.dailyQuestionRightAnswer

                if (quizOptionCorrectButtonColor in dailychallengeUiState.value.optionsColors) return@launch

                val optionsList = question.options
                when (clickedOptionName) {
                    optionsList[0] -> {
                        if (currectOption == clickedOptionName) updateButton(
                            OptionsButton.First,
                            OptionState.Correct
                        )
                        else {
                            updateButton(OptionsButton.First, OptionState.Wrong)
                            when (optionsList.indexOf(currectOption)) {
                                1 -> updateButton(OptionsButton.Second, OptionState.Correct)
                                2 -> updateButton(OptionsButton.Third, OptionState.Correct)
                                3 -> updateButton(OptionsButton.Fourth, OptionState.Correct)
                            }
                        }
                    }

                    optionsList[1] -> {
                        if (currectOption == clickedOptionName) {
                            updateButton(OptionsButton.Second, OptionState.Correct)
                        } else {
                            updateButton(OptionsButton.Second, OptionState.Wrong)
                            when (optionsList.indexOf(currectOption)) {
                                0 -> updateButton(OptionsButton.First, OptionState.Correct)
                                2 -> updateButton(OptionsButton.Third, OptionState.Correct)
                                3 -> updateButton(OptionsButton.Fourth, OptionState.Correct)
                            }
                        }
                    }

                    optionsList[2] -> {
                        if (currectOption == clickedOptionName) {
                            updateButton(OptionsButton.Third, OptionState.Correct)
                        } else {
                            updateButton(OptionsButton.Third, OptionState.Wrong)
                            when (optionsList.indexOf(currectOption)) {
                                0 -> updateButton(OptionsButton.First, OptionState.Correct)
                                1 -> updateButton(OptionsButton.Second, OptionState.Correct)
                                3 -> updateButton(OptionsButton.Fourth, OptionState.Correct)
                            }
                        }
                    }

                    optionsList[3] -> {
                        if (currectOption == clickedOptionName) {
                            updateButton(OptionsButton.Fourth, OptionState.Correct)
                        } else {
                            updateButton(OptionsButton.Fourth, OptionState.Wrong)
                            when (optionsList.indexOf(currectOption)) {
                                0 -> updateButton(OptionsButton.First, OptionState.Correct)
                                1 -> updateButton(OptionsButton.Second, OptionState.Correct)
                                2 -> updateButton(OptionsButton.Third, OptionState.Correct)
                            }
                        }
                    }
                }

                val coins = repo.loadUserCoinsFlow().first()
                val actualDailyQuestionId = dailyChallengeRepository.loadActualDailyQuestionId()
                val savedDailyQuestions = dailyChallengeRepository.loadSavedDailyQuestions()

                if (currectOption == clickedOptionName) {
                    sendSound(SoundEvent.Correct)

                    delay(1.7.seconds)
                    repo.saveUserCoins(coins + 5)
                    updateState {
                        it.copy(
                            dailyChallengeMessage = DailyChallengeMessage.RightAnswer(
                                message = "Respondeu correctamente o desafio de hoje",
                                earnedCoins = "+5"
                            ),
                    ) }
                } else {
                    sendSound(SoundEvent.Wrong)

                    delay(1.7.seconds)
                    repo.saveUserCoins(coins + 1)
                    updateState { it.copy(dailyChallengeMessage = DailyChallengeMessage.WrongAnswer(
                        message = "Não respondeu correctamente o desafio de hoje",
                        rightAnswerText = currectOption,
                        earnedCoins = "+1"
                    )) }
                }
                val timestamp = System.currentTimeMillis()

                dailyChallengeRepository.saveDailyQuestions(savedDailyQuestions + actualDailyQuestionId)
                dailyChallengeRepository.saveLastDateTimeUserGotDailyQuestionId(timestamp)
                dailyChallengeRepository.saveActualDailyQuestionId("")

                updateState {
                    it.copy(
                        dailyQuestionId = null,
                        lastDateUserGotQuestion = timestamp
                    )
                }
                // Once daily challenge done, user does not need daily notification
                dailyNotificationScheduler.cancelDailyNotification()
            }
        }
    }

    fun onPrintScreen(context: Context, graphicsLayer: GraphicsLayer) {
        viewModelScope.launch {
            updateState { it.copy(showBottomBar = false) }
            val imageBitmap = graphicsLayer.toImageBitmap()
            val bitmap = imageBitmap.asAndroidBitmap()
            val uri = saveBitmap(context, bitmap)
            shareImage(context, uri)

            delay(5.seconds)
            updateState { it.copy(showBottomBar = true) }
        }
    }

    fun onCloseMessageContainer() = updateState { it.copy(dailyChallengeMessage = DailyChallengeMessage.Empty) }
}