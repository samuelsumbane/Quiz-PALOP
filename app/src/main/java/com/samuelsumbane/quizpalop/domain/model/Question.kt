package com.samuelsumbane.quizpalop.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class Question(
    val id: String,
    val question: String,
    val questionLevel: String,
    val options: List<String>,
    val correctIndex: Int
)