package com.samuelsumbane.quizpalop.presentation.maingamepage

import androidx.lifecycle.viewModelScope
import com.samuelsumbane.quizpalop.domain.model.ChangeCountValues
import com.samuelsumbane.quizpalop.domain.model.UserCoins
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.hours


fun MainGameViewModel.buyLifeWithCoins(requiredCoins: Int, respectiveLives: Int) {
    if (mainGameUiState.value.userCoins >= requiredCoins) {
        changeUserCoins(UserCoins.DecreaseCoins(requiredCoins))
        changeLivesCount(ChangeCountValues.IncreaseLives(respectiveLives))
//        changeTimerState(QuestionTimerState.Running)
        clearLastDateTimeUserLostLives()
    }
}
fun MainGameViewModel.saveLives(qtd: Int) {
    viewModelScope.launch { settingsManager.saveIntValues(settingsManager.lives, qtd) }
}

fun MainGameViewModel.changeLivesCount(liveState: ChangeCountValues) {
    val lives = mainGameUiState.value.lives

    when (liveState) {
        is ChangeCountValues.IncreaseLives -> saveLives(lives + liveState.plusNum)
        ChangeCountValues.DecreaseLive -> {
            if (lives > 0) {
                if (lives == 1) {
                    val triggerTime = System.currentTimeMillis() + 2.hours.inWholeMilliseconds
                    lifeNotificationScheduler.scheduleNotification(triggerTime)
                } else {
                    lifeNotificationScheduler.cancelNotification()
                }
                saveLives(lives - 1)
            }
        }
    }
}

fun MainGameViewModel.clearLastDateTimeUserLostLives() {
    viewModelScope.launch {
        updateState { it.copy(lastDateTimeLostLives = 0L) }
        settingsManager.saveLongValue(settingsManager.lastDateTimeLostLives, 0L)
    }
}

fun MainGameViewModel.watchAdsAndLoadnextQuestion() {
    changeLivesCount(ChangeCountValues.IncreaseLives(1))
    setGameTextMessage(GameTextMessage.NewLifeEarned("Parabéns, ganhou +1 vida."))
}