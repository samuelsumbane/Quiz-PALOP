package com.samuelsumbane.quizpalop.domain.model

sealed class UserCoins() {
    data class IncreaseCoins(val coinsToIncrease: Int) : UserCoins()
    data class DecreaseCoins(val coinsToDecrease: Int) : UserCoins()
}