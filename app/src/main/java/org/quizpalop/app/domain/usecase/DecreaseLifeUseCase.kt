package org.quizpalop.app.domain.usecase

import org.quizpalop.app.domain.repository.LifeNotificationScheduler
import org.quizpalop.app.domain.repository.QuizRepository
import kotlin.time.Duration.Companion.hours

class DecreaseLifeUseCase(
    private val quizRepository: QuizRepository,
    private val lifeNotificationScheduler: LifeNotificationScheduler
) {
    suspend operator fun invoke(userLives: Int, postNotification: Boolean) {
        if (userLives == 1) {
            val triggerTime = System.currentTimeMillis() + 2.hours.inWholeMilliseconds
            if (postNotification) lifeNotificationScheduler.scheduleNotification(triggerTime)
        } else {
            lifeNotificationScheduler.cancelNotification()
        }
        quizRepository.saveUserLives(userLives)
    }
}