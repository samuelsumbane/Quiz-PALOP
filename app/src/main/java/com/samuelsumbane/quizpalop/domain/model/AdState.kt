package com.samuelsumbane.quizpalop.domain.model

enum class AdState(val stateName: String) {
    Loading("Carregando..."),
    Ready("Ver"),
    Error("Carregar")
}