package com.samuelsumbane.quizpalop.domain.model

sealed class ChangeCountValues {
    data class IncreaseLives(val plusNum: Int) : ChangeCountValues()
    data object DecreaseLive : ChangeCountValues() // It decrease 1 per time
}