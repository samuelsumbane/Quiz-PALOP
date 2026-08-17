package org.quizpalop.app.presentation.maingamepage

import androidx.lifecycle.viewModelScope
import org.quizpalop.app.domain.model.ChangeLifeCountValues
import org.quizpalop.app.domain.model.UserCoins
import kotlinx.coroutines.launch


fun MainGameViewModel.buyLifeWithCoins(requiredCoins: Int, respectiveLives: Int) {
    if (mainGameUiState.value.userCoins >= requiredCoins) {
        changeUserCoins(UserCoins.DecreaseCoins(requiredCoins))
        changeLivesCount(ChangeLifeCountValues.IncreaseLives(respectiveLives))
//        changeTimerState(QuestionTimerState.Running)
        clearLastDateTimeUserLostLives()
    }
}

fun MainGameViewModel.changeLivesCount(lifeState: ChangeLifeCountValues) {
    val lives = mainGameUiState.value.lives ?: return

    when (lifeState) {
        is ChangeLifeCountValues.IncreaseLives -> viewModelScope.launch { repo.saveUserLives(lives + lifeState.plusNum) }
        ChangeLifeCountValues.DecreaseLife -> {
            viewModelScope.launch {
                val postNotification = userPreferencesRepository.loadPostNotifications()
                if (lives > 0) decreaseLifeUseCase(lives, postNotification = postNotification)
            }
        }
    }
}

fun MainGameViewModel.clearLastDateTimeUserLostLives() {
    viewModelScope.launch {
        updateState { it.copy(lastDateTimeLostLives = 0L) }
        repo.saveLastDateTimeUserLostLives(0L)
        lifeNotificationScheduler.cancelNotification()
    }
}

fun MainGameViewModel.watchAdsAndLoadNextQuestion() {
    changeLivesCount(ChangeLifeCountValues.IncreaseLives(1))
    setGameTextMessage(GameTextMessage.NewLifeEarned("Parabéns, ganhou +1 vida."))
    clearLastDateTimeUserLostLives()
}