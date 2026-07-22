package com.samuelsumbane.quizpalop.presentation.dailychallenge

sealed class DailyChallengeMessage {
    data object Empty : DailyChallengeMessage()
    data class RightAnswer(
        val title: String,
        val message: String,
        val earnedCoins: String,
    ) : DailyChallengeMessage()
    data class WrongAnswer(
        val title: String,
        val message: String,
        val rightAnswerText: String,
        val earnedCoins: String
    ) : DailyChallengeMessage()
}