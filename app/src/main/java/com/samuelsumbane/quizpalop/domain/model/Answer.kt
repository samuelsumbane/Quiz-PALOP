package com.samuelsumbane.quizpalop.domain.model

data class Answer(
    val questionId: String,
    val text: String,
    val isCorrect: Boolean
)
