package com.samuelsumbane.quizpalop.presentation.maingamepage

import androidx.lifecycle.viewModelScope
import com.samuelsumbane.quizpalop.core.notifications.NotificationScheduler
import com.samuelsumbane.quizpalop.domain.model.ChangeCountValues
import com.samuelsumbane.quizpalop.domain.model.UserCoins
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes


fun MainGameViewModel.buyLifeWithCoins(requiredCoins: Int, respectiveLives: Int) {
    if (mainGameUiState.value.userCoins >= requiredCoins) {
        changeUserCoins(UserCoins.DecreaseCoins(requiredCoins))
        changeLivesCount(ChangeCountValues.IncreaseLives(respectiveLives))
//        changeTimerState(QuestionTimerState.Running)
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
                    lifeNotificationScheduler.scheduleNotification(5.minutes.inWholeMilliseconds)
                } else {
                    lifeNotificationScheduler.cancelNotification()
                }
                saveLives(lives - 1)
            }
        }
    }

}