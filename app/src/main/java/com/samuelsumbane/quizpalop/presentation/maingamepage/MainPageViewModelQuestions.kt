package com.samuelsumbane.quizpalop.presentation.maingamepage

import androidx.lifecycle.viewModelScope
import com.samuelsumbane.quizpalop.domain.model.Category
import com.samuelsumbane.quizpalop.domain.model.ChangeCountValues
import com.samuelsumbane.quizpalop.domain.model.Countries
import com.samuelsumbane.quizpalop.domain.model.QuestionTimerState
import com.samuelsumbane.quizpalop.domain.model.SoundEvent
import com.samuelsumbane.quizpalop.domain.model.UserCoins
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

            updateState { it.copy(timerState = QuestionTimerState.Stop) }

            if (currectOption == clickedOptionName) {
                viewModelScope.launch {
                    updateState { it.copy(questionsIdList = it.questionsIdList - question.id) }

                    if (!mainGameUiState.value.userGotHelp) {
                        updateState { it.copy(answeredQuestionsWithoutMistake = it.answeredQuestionsWithoutMistake + 1) }
                        giveCoinsToUser()
                    }

                    val answeredQuestions = mainGameUiState.value.answeredQuestionsList + question.id
                    updateState { it.copy(answeredQuestionsList = answeredQuestions) }
//                println("ouvindo: to save: ${mainGameUiState.value.answeredQuestionsList}")
                    settingsManager.saveStringsValues(settingsManager.savedQuestionsList,mainGameUiState.value.answeredQuestionsList)

                    sendSound(SoundEvent.Correct)
                    startLoadingNextQuestion()
                }
            } else {
                viewModelScope.launch {
                    changeLivesCount(ChangeCountValues.DecreaseLive)
                    clearAnsweredQuestionsWithoutMistake()
                    setLastDateTimeUserLostLives()
                    if (mainGameUiState.value.userCoins == 0) changeTimerState(QuestionTimerState.Stop)
                    sendSound(SoundEvent.Wrong)
                    startLoadingNextQuestion()
                }
            }

            changeUserHelpState(false)
        }
    }
}

fun MainGameViewModel.setLastDateTimeUserLostLives() {
    viewModelScope.launch {
        val timeStamp = System.currentTimeMillis()
        settingsManager.saveLongValue(settingsManager.lastDateTimeLostLives, timeStamp)
        updateState { it.copy(lastDateTimeLostLives = timeStamp) }
    }
}

fun MainGameViewModel.clearAnsweredQuestionsWithoutMistake() = updateState { it.copy(answeredQuestionsWithoutMistake = 0) }

fun MainGameViewModel.clearLastDateTimeLostLives() {
    viewModelScope.launch {
        settingsManager.saveLongValue(settingsManager.lastDateTimeLostLives, 0L)
        updateState { it.copy(lastDateTimeLostLives = 0L) }
    }
}

fun MainGameViewModel.getCountryAndCategoryQuestions(country: Countries, category: Category): Set<String> {
   return mainGameUiState.value.allQuestions
       .filter { it.id.startsWith(country.code) && it.questionLevel == category.categoryMeaning }
       .map { it.id }
       .toSet()
}

fun MainGameViewModel.saveLoadConfigs(country: Countries, category: Category) {
    viewModelScope.launch {
        val savedQuestions = mainGameUiState.value.answeredQuestionsList
        val notAnsweredQuestions =
            getCountryAndCategoryQuestions(country, category) - savedQuestions
        updateState {
            it.copy(
                questionsIdList = notAnsweredQuestions,
                selectedCountry = country, selectedCategory = category
            )
        }
        settingsManager.saveStringValues(settingsManager.lastSelectedCategory, category.categoryName)
        settingsManager.saveStringValues(settingsManager.lastSelectedCountry, country.countryName)

        loadNextQuestion()
    }
}

fun MainGameViewModel.tryToLoadNextCategoryInThisCategory() {
    viewModelScope.launch {
        mainGameUiState.value.selectedCountry?.let { selectedCountry ->
            mainGameUiState.value.selectedCategory?.let { selectedCategory ->

                when (val category = selectedCategory) {
                    Category.History -> {
                        when (selectedCountry) {
                            Countries.Angola -> saveLoadConfigs(Countries.Cv, category)
                            Countries.Cv -> saveLoadConfigs(Countries.Gw, category)
                            Countries.Gw -> saveLoadConfigs(Countries.Mz, category)
                            Countries.Mz -> saveLoadConfigs(Countries.Stp, category)
                            Countries.Stp -> {
                                // Check if all easy level is answered
//                                if ()
                                saveLoadConfigs(Countries.Angola, Category.Culture)
                            }
                        }
                    }

                    Category.Culture -> {
                        when (selectedCountry) {
                            Countries.Angola -> saveLoadConfigs(Countries.Cv, category)
                            Countries.Cv -> saveLoadConfigs(Countries.Gw, category)
                            Countries.Gw -> saveLoadConfigs(Countries.Mz, category)
                            Countries.Mz -> saveLoadConfigs(Countries.Stp, category)
                            Countries.Stp -> saveLoadConfigs(Countries.Angola, Category.Exam)
                        }
                    }

                    Category.Exam -> {
                        when (val countryForExame = selectedCountry) {
                            Countries.Angola -> saveLoadConfigs(Countries.Cv, category)
                            Countries.Cv -> saveLoadConfigs(Countries.Gw, category)
                            Countries.Gw -> saveLoadConfigs(Countries.Mz, category)
                            Countries.Mz -> saveLoadConfigs(Countries.Stp, category)
                            Countries.Stp -> saveLoadConfigs(Countries.Angola, Category.History)
                        }
                    }
                }

            }

        }
    }
}


fun MainGameViewModel.giveCoinsToUser() {
    when (mainGameUiState.value.answeredQuestionsWithoutMistake) {
        5 -> {
            changeUserCoins(UserCoins.IncreaseCoins(2))
            setAddCoinsMessage("Bónus: +2 moedas")
        }
        10 -> {
            changeUserCoins(UserCoins.IncreaseCoins(5))
            setAddCoinsMessage("Boa sequência! +5 moedas")
        }
        15 -> {
            changeUserCoins(UserCoins.IncreaseCoins(8))
            setAddCoinsMessage("Boa sequência! +8 moedas")
        }
        20 -> {
            changeUserCoins(UserCoins.IncreaseCoins(12))
            setAddCoinsMessage("Grande sequência +12 moedas")
        }
        30 -> {
            changeUserCoins(UserCoins.IncreaseCoins(18))
            setAddCoinsMessage("Excelente sequência +18 moedas")
        }
        40 -> {
            changeUserCoins(UserCoins.IncreaseCoins(25))
            setAddCoinsMessage("Excelente sequência +25 moedas")
        }
        50 -> {
            changeUserCoins(UserCoins.IncreaseCoins(35))
            setAddCoinsMessage("Sequência lendária! +35 moedas")
        }
    }
}

fun MainGameViewModel.showCurrectOptionAfterViewAd() {
    mainGameUiState.value.actualQuestion?.let {
        setGameTextMessage(
            GameTextMessage.ShowRightAnswer("""A resposta correcta é: "${mainGameUiState.value.actualQuestionRightAnswer}" """)
        )
    }
}

fun MainGameViewModel.setQuestionWrong() {
    if (mainGameUiState.value.timerState == QuestionTimerState.Stop) return
    viewModelScope.launch {
        updateState {
            it.copy(gameTextMessage = GameTextMessage.QuestionNotAnswered("Tempo estogado", "O cronômetro venceu você dessa vez e levou uma vida\n Seja mais rápido na próxima!"))
        }
        settingsManager.saveIntValues(settingsManager.lives, mainGameUiState.value.lives - 1)

        if (mainGameUiState.value.lives == 0) {
            setLastDateTimeUserLostLives()
            setGameTextMessage(GameTextMessage.Empty)
        }
    }
}