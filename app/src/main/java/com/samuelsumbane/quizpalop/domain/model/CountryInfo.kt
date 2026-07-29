package com.samuelsumbane.quizpalop.domain.model

import com.samuelsumbane.quizpalop.data.repository.countriesData

data class CountryInfo(
    val country: Countries,
    val flagPath: Int,
    val capital: String,
    val corrency: String,
    val independencia: String,
    val area: String,
    val paisesVisinhos: String,
    val fusoHorario: String
)