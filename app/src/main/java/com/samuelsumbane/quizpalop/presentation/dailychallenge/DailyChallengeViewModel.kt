package com.samuelsumbane.quizpalop.presentation.dailychallenge

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelsumbane.quizpalop.core.isDifferentDay
import com.samuelsumbane.quizpalop.core.saveBitmap
import com.samuelsumbane.quizpalop.core.shareImage
import com.samuelsumbane.quizpalop.domain.model.SoundEvent
import com.samuelsumbane.quizpalop.domain.repository.QuizRepository
import com.samuelsumbane.quizpalop.domain.repository.SettingsManager
import com.samuelsumbane.quizpalop.presentation.composables.PageUiState
import com.samuelsumbane.quizpalop.presentation.maingamepage.OptionState
import com.samuelsumbane.quizpalop.presentation.maingamepage.OptionsButton
import com.samuelsumbane.quizpalop.presentation.maingamepage.quizOptionCurrectButtonColor
import com.samuelsumbane.quizpalop.presentation.maingamepage.quizOptionWrongButtonColor
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
    private val settingsManager: SettingsManager,
    private val repo: QuizRepository
) : ViewModel() {
    private val _dailyChallengeViewModel = MutableStateFlow(DailyChallengeUiState())
    val dailychallengeUiState = _dailyChallengeViewModel.asStateFlow()
    private val _soundEvent = Channel<SoundEvent>()
    val soundEvent = _soundEvent.receiveAsFlow()


    fun resetState() {
        _dailyChallengeViewModel.value = DailyChallengeUiState()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAllSavedDailyQuestions() {
        viewModelScope.launch {
//            val lastDateUserGotDailyQuestion = 1753254000000L
            val now = System.currentTimeMillis()
            val lastDateUserGotDailyQuestion = settingsManager.readLongValues(settingsManager.lastDateTimeUserGotDailyQuestionId)

            if (isDifferentDay(lastDateUserGotDailyQuestion, now)) {
                val actualDailyQuestionId = settingsManager.readStringValues(settingsManager.actualDailyQuestionId).first()
                println("estado: actualId is: $actualDailyQuestionId")
                val allQuestions = repo.getQuestions()
                println("estado: allQuestion $allQuestions")
                val savedDailyQuestions =
                    settingsManager.readSavedStringsValues(settingsManager.savedDailyQuestions)
                        .first()
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

                val mediumQuestionsPercentage =
                    mediumSavedQuestions.size.toFloat() / allMediumQuestions.size
                val hardQuestionsPercentage =
                    hardSavedQuestions.size.toFloat() / allHardQuestions.size


                val allNotAnsweredQuestions = allQuestionsId subtract savedDailyQuestions
                if (allNotAnsweredQuestions.isEmpty()) {
                    settingsManager.saveStringsValues(
                        settingsManager.savedDailyQuestions,
                        emptySet()
                    )
                }

                val availablesQuestionsId = when {
                    mediumQuestionsPercentage < 1.0f -> allNotAnsweredQuestions subtract mediumSavedQuestions
                    hardQuestionsPercentage < 1.0f -> allNotAnsweredQuestions subtract hardSavedQuestions
                    else -> {
                        println("estado: m $mediumQuestionsPercentage, h: $hardQuestionsPercentage")
                        allNotAnsweredQuestions
                    }
                }

                val randomedQuestionId = availablesQuestionsId.random()
                println("estado: randomized is: $randomedQuestionId")
                    val randomedQuestion =
                        allQuestions.first { it.id == randomedQuestionId }
                    updateState {
                        it.copy(
                            dailyQuestionId = randomedQuestion.id,
                            dailyChallengeLoadQuestionState = DailyChallengeLoadQuestionState.FINISHED
                        )
                    }
                    settingsManager.saveStringValues(
                        settingsManager.actualDailyQuestionId,
                        randomedQuestion.id
                    )
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

                val coins = settingsManager.readIntValues(settingsManager.userCoins).first()
                val actualDailyQuestionId = settingsManager.readStringValues(settingsManager.actualDailyQuestionId).first()
                val savedDailyQuestions = settingsManager.readSavedStringsValues(settingsManager.savedDailyQuestions).first()

                if (currectOption == clickedOptionName) {
                    sendSound(SoundEvent.Correct)
                    delay(1.7.seconds)
                    settingsManager.saveIntValues(settingsManager.userCoins, coins + 5)
                    updateState {
                        it.copy(
                            dailyChallengeMessage = DailyChallengeMessage.RightAnswer(
                                title = "Parabéns!!!",
                                message = "Acertou correctamente o desafio de hoje",
                                earnedCoins = "+5"
                            ),
                    ) }
                } else {
                    sendSound(SoundEvent.Wrong)
                    delay(1.7.seconds)
                    settingsManager.saveIntValues(settingsManager.userCoins, coins + 1)
                    updateState { it.copy(dailyChallengeMessage = DailyChallengeMessage.WrongAnswer(
                        title = "Sem sucesso",
                        message = "Não respondeu correctamente o desafio de hoje",
                        rightAnswerText = currectOption,
                        earnedCoins = "+1"
                    )) }
                }
                val timestamp = System.currentTimeMillis()

                settingsManager.saveStringsValues(settingsManager.savedDailyQuestions, savedDailyQuestions + actualDailyQuestionId)
                settingsManager.saveLongValue(settingsManager.lastDateTimeUserGotDailyQuestionId, timestamp)
                settingsManager.saveStringValues(settingsManager.actualDailyQuestionId, "")

                updateState {
                    it.copy(
                        dailyQuestionId = null,
                        lastDateUserGotQuestion = timestamp
                    )
                }
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