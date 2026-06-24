package com.samuelsumbane.quizpalop.core

import android.content.Context
import com.samuelsumbane.quizpalop.domain.model.Question
import kotlinx.serialization.json.Json
import okio.IOException


fun loadQuestionsFromAssets(context: Context): List<Question> {
    return try {
        val jsonString = context.assets
            .open("mozambique/historia_basica.json")
            .bufferedReader()
            .use { it.readText() }
        Json.decodeFromString<List<Question>>(jsonString)
    } catch (e: IOException) {
        emptyList()
    }
}