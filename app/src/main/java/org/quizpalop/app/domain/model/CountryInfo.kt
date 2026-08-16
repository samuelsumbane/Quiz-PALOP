package org.quizpalop.app.domain.model

data class CountryInfo(
    val country: Countries,
    val flagPath: Int,
    val capital: String,
    val currency: String,
    val independencia: String,
    val area: String,
    val paisesVisinhos: String,
    val fusoHorario: String
)