package com.samuelsumbane.quizpalop.domain.model

enum class Category(
    val categoryName: String,
    val categoryMeaning: String,
) {
    History("História Básica", "Easy"),
    Culture("Cultura Geral", "Medium"),
    Exam("Exame/Entrevista", "Hard")
}