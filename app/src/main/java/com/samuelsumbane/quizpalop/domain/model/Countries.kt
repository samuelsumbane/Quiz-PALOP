package com.samuelsumbane.quizpalop.domain.model

enum class Countries(
    val code: String,
    val countryName: String
) {
    Angola("ao","Angola"),
    Cv("cv", "Cabo Verde"),
    Gw("gw", "Guiné-Bissau"),
    Stp("stp", "São Tomé e Príncipe"),
    Ge("get", "Guiné Equatorial"),
    Mz("mz", "Moçambique"),
    None("nn", "None")
}