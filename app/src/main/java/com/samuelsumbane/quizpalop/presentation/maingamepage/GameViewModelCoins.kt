package com.samuelsumbane.quizpalop.presentation.maingamepage

import androidx.lifecycle.viewModelScope
import com.samuelsumbane.quizpalop.domain.model.UserCoins
import kotlinx.coroutines.launch

fun MainGameViewModel.changeUserCoins(coinOption: UserCoins) {
    viewModelScope.launch {
        when (coinOption) {
            is UserCoins.IncreaseCoins -> updateState { it.copy(userCoins = it.userCoins + coinOption.coinsToIncrease) }

            is UserCoins.DecreaseCoins -> updateState { it.copy(userCoins = it.userCoins - coinOption.coinsToDecrease) }
        }
        settingsManager.saveIntValues(settingsManager.userCoins, mainGameUiState.value.userCoins)
    }
}
