package org.quizpalop.app.presentation.maingamepage

import androidx.lifecycle.viewModelScope
import org.quizpalop.app.domain.model.UserCoins
import kotlinx.coroutines.launch

fun MainGameViewModel.changeUserCoins(coinOption: UserCoins) {
    viewModelScope.launch {
        when (coinOption) {
            is UserCoins.IncreaseCoins -> updateState { it.copy(userCoins = it.userCoins + coinOption.coinsToIncrease) }

            is UserCoins.DecreaseCoins -> updateState { it.copy(userCoins = it.userCoins - coinOption.coinsToDecrease) }
        }
        repo.saveUserCoins(mainGameUiState.value.userCoins)
    }
}
