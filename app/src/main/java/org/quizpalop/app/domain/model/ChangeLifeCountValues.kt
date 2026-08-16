package org.quizpalop.app.domain.model

sealed class ChangeLifeCountValues {
    data class IncreaseLives(val plusNum: Int) : ChangeLifeCountValues()
    data object DecreaseLife : ChangeLifeCountValues() // It decreases 1 life per time
}