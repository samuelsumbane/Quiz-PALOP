package org.quizpalop.app.presentation.maingamepage

import org.quizpalop.app.domain.model.HelpOption



sealed class GameTextMessage {
    data class AddedCoins(val message: String) : GameTextMessage()
    data class QuestionNotAnswered(val title: String, val message: String) : GameTextMessage()
    data class ExitGame(val confirmationText: String) : GameTextMessage()
    data object Empty : GameTextMessage()
    data class AllQuestionsAnswered(val title: String, val message: String) : GameTextMessage()
    data class SelectedQuestionsAnswered(val title: String, val message: String, val confirmationText: String) : GameTextMessage()
    data class CannotGetHelp(val reasonTitle: String, val reasonMessage: String, val helpOption: HelpOption) : GameTextMessage()
    data class NewLifeEarned(val message: String) : GameTextMessage()
    data class ShowRightAnswer(val message: String) : GameTextMessage()
}
