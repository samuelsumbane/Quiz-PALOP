package com.samuelsumbane.quizpalop.presentation.dailychallenge

sealed class DailyChallengeMessage {
    data object Empty : DailyChallengeMessage()
    data class RightAnswer(
        val message: String,
        val earnedCoins: String,
    ) : DailyChallengeMessage()
    data class WrongAnswer(
        val message: String,
        val rightAnswerText: String,
        val earnedCoins: String
    ) : DailyChallengeMessage()
}