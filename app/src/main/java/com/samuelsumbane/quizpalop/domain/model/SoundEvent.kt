package com.samuelsumbane.quizpalop.domain.model

sealed class SoundEvent {
    data object Correct : SoundEvent()
    data object Wrong : SoundEvent()
    data object Click : SoundEvent()
    data object CoinEarned: SoundEvent()
}