package com.samuelsumbane.quizpalop.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class Question(
    val id: String,
    val question: String,
    val questionLevel: String,
    val options: List<String>,
    val correctIndex: Int
) {
    fun getCountry(): Countries {
        return when (id[0]) {
            'a' -> Countries.Angola
            'c' -> Countries.Cv
            'g' -> Countries.Gw
            'm' -> Countries.Mz
            else -> Countries.Stp
        }
    }

    fun getCategory(): Category {
        return when (questionLevel) {
            "Easy" -> Category.History
            "Medium" -> Category.Culture
            else -> Category.Exam
        }
    }
}

