package com.samuelsumbane.quizpalop.domain.model

enum class Category(
    val categoryName: String,
    val categoryMeaning: String,
) {
    History("História Básica(fácil)", "Easy"),
    Culture("Cultura Geral(médio)", "Medium"),
    Exam("Exame/Entrevista(difícil)", "Hard")
}