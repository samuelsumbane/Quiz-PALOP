package org.quizpalop.app.presentation.maingamepage

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.quizpalop.app.domain.model.Category
import org.quizpalop.app.domain.model.ChangeLifeCountValues
import org.quizpalop.app.domain.model.Countries
import org.quizpalop.app.domain.model.QuestionTimerState
import org.quizpalop.app.domain.model.SoundEvent
import org.quizpalop.app.domain.model.UserCoins


enum class OptionsButton { First, Second, Third, Fourth }
enum class OptionState { Correct, Wrong }

fun MainGameViewModel.updateButton(button: OptionsButton, state: OptionState) {
    val newColor = if (state == OptionState.Correct) quizOptionCorrectButtonColor else quizOptionWrongButtonColor
    when (button) {
        OptionsButton.First -> updateState { it.copy(optionsColors = it.optionsColors.mapIndexed { index, color -> if (index == 0) newColor else color }) }
        OptionsButton.Second -> updateState { it.copy(optionsColors = it.optionsColors.mapIndexed { index, color -> if (index == 1) newColor else color }) }
        OptionsButton.Third -> updateState { it.copy(optionsColors = it.optionsColors.mapIndexed { index, color -> if (index == 2) newColor else color }) }
        OptionsButton.Fourth -> updateState { it.copy(optionsColors = it.optionsColors.mapIndexed { index, color -> if (index == 3) newColor else color }) }
    }
}

fun MainGameViewModel.checkResponse(clickedOptionName: String) {
    viewModelScope.launch {
        mainGameUiState.value.actualQuestion?.let { question ->
            val correctOption = mainGameUiState.value.actualQuestionRightAnswer

            val optionsList = question.options
            when (clickedOptionName) {
                optionsList[0] -> {
                    if (correctOption == clickedOptionName) updateButton(
                        OptionsButton.First,
                        OptionState.Correct
                    )
                    else {
                        updateButton(OptionsButton.First, OptionState.Wrong)
                        when (optionsList.indexOf(correctOption)) {
                            1 -> updateButton(OptionsButton.Second, OptionState.Correct)
                            2 -> updateButton(OptionsButton.Third, OptionState.Correct)
                            3 -> updateButton(OptionsButton.Fourth, OptionState.Correct)
                        }
                    }
                }

                optionsList[1] -> {
                    if (correctOption == clickedOptionName) {
                        updateButton(OptionsButton.Second, OptionState.Correct)
                    } else {
                        updateButton(OptionsButton.Second, OptionState.Wrong)
                        when (optionsList.indexOf(correctOption)) {
                            0 -> updateButton(OptionsButton.First, OptionState.Correct)
                            2 -> updateButton(OptionsButton.Third, OptionState.Correct)
                            3 -> updateButton(OptionsButton.Fourth, OptionState.Correct)
                        }
                    }
                }

                optionsList[2] -> {
                    if (correctOption == clickedOptionName) {
                        updateButton(OptionsButton.Third, OptionState.Correct)
                    } else {
                        updateButton(OptionsButton.Third, OptionState.Wrong)
                        when (optionsList.indexOf(correctOption)) {
                            0 -> updateButton(OptionsButton.First, OptionState.Correct)
                            1 -> updateButton(OptionsButton.Second, OptionState.Correct)
                            3 -> updateButton(OptionsButton.Fourth, OptionState.Correct)
                        }
                    }
                }

                optionsList[3] -> {
                    if (correctOption == clickedOptionName) {
                        updateButton(OptionsButton.Fourth, OptionState.Correct)
                    } else {
                        updateButton(OptionsButton.Fourth, OptionState.Wrong)
                        when (optionsList.indexOf(correctOption)) {
                            0 -> updateButton(OptionsButton.First, OptionState.Correct)
                            1 -> updateButton(OptionsButton.Second, OptionState.Correct)
                            2 -> updateButton(OptionsButton.Third, OptionState.Correct)
                        }
                    }
                }
            }

            updateState { it.copy(timerState = QuestionTimerState.Stop) }

            if (correctOption == clickedOptionName) {
                viewModelScope.launch {
                    updateState { it.copy(questionsIdList = it.questionsIdList - question.id) }


                    val answeredQuestions = mainGameUiState.value.answeredQuestionsList + question.id
                    updateState { it.copy(answeredQuestionsList = answeredQuestions) }
                    repo.saveQuestionsList(mainGameUiState.value.answeredQuestionsList)
                    vibrateOnSuccess()
                    sendSound(SoundEvent.Correct)
//                    startLoadingNextQuestion()
                    if (!mainGameUiState.value.userGotHelp) {
                        updateState { it.copy(answeredQuestionsWithoutMistake = it.answeredQuestionsWithoutMistake + 1) }
                        giveCoinsToUser()
                    }
                }
            } else {
                viewModelScope.launch {
                    changeLivesCount(ChangeLifeCountValues.DecreaseLife)
                    clearAnsweredQuestionsWithoutMistake()
                    setLastDateTimeUserLostLives()
                    if (mainGameUiState.value.userCoins == 0) changeTimerState(QuestionTimerState.Stop)
                    vibrateOnError()
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
        repo.saveLastDateTimeUserLostLives(timeStamp)
        updateState { it.copy(lastDateTimeLostLives = timeStamp) }
    }
}

fun MainGameViewModel.clearAnsweredQuestionsWithoutMistake() = updateState { it.copy(answeredQuestionsWithoutMistake = 0) }

fun MainGameViewModel.clearLastDateTimeLostLives() {
    viewModelScope.launch {
        repo.saveLastDateTimeUserLostLives(0L)
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
        questionsConfigRepository.saveCategory(category.categoryName)
        questionsConfigRepository.saveCountry(country.countryName)

        loadNextQuestion()
    }
}

fun MainGameViewModel.tryToLoadNextCategoryInThisCategory() {
    viewModelScope.launch {
        mainGameUiState.value.selectedCountry?.let { selectedCountry ->
            mainGameUiState.value.selectedCategory?.let { selectedCategory ->

                when (selectedCategory) {
                    Category.History -> {
                        when (selectedCountry) {
                            Countries.Angola -> saveLoadConfigs(Countries.Cv, Category.History)
                            Countries.Cv -> saveLoadConfigs(Countries.Gw, Category.History)
                            Countries.Gw -> saveLoadConfigs(Countries.Mz, Category.History)
                            Countries.Mz -> saveLoadConfigs(Countries.Stp, Category.History)
                            Countries.Stp -> saveLoadConfigs(Countries.Angola, Category.Culture)
                        }
                    }

                    Category.Culture -> {
                        when (selectedCountry) {
                            Countries.Angola -> saveLoadConfigs(Countries.Cv, Category.Culture)
                            Countries.Cv -> saveLoadConfigs(Countries.Gw, Category.Culture)
                            Countries.Gw -> saveLoadConfigs(Countries.Mz, Category.Culture)
                            Countries.Mz -> saveLoadConfigs(Countries.Stp, Category.Culture)
                            Countries.Stp -> saveLoadConfigs(Countries.Angola, Category.Exam)
                        }
                    }

                    Category.Exam -> {
                        when (selectedCountry) {
                            Countries.Angola -> saveLoadConfigs(Countries.Cv, Category.Exam)
                            Countries.Cv -> saveLoadConfigs(Countries.Gw, Category.Exam)
                            Countries.Gw -> saveLoadConfigs(Countries.Mz, Category.Exam)
                            Countries.Mz -> saveLoadConfigs(Countries.Stp, Category.Exam)
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
        else -> startLoadingNextQuestion()
    }
}

fun MainGameViewModel.showCorrectOptionAfterViewAd() {
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
            it.copy(gameTextMessage = GameTextMessage.QuestionNotAnswered("Tempo esgotado", "O cronômetro venceu você dessa vez e levou uma vida\n Seja mais rápido na próxima!"))
        }
        val postNotification = userPreferencesRepository.loadPostNotifications()
        if (mainGameUiState.value.lives > 0) decreaseLifeUseCase(mainGameUiState.value.lives, postNotification)

        if (mainGameUiState.value.lives == 0) {
            setLastDateTimeUserLostLives()
            setGameTextMessage(GameTextMessage.Empty)
        }
    }
}