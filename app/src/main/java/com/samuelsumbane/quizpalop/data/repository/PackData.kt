package com.samuelsumbane.quizpalop.data.repository

import com.samuelsumbane.quizpalop.domain.model.Country
import com.samuelsumbane.quizpalop.domain.model.Pack
import com.samuelsumbane.quizpalop.domain.model.QuestionDifficulty

val mz = Country("Moçambique", "")
val ao = Country("Angola", "")
val cv = Country("Cabo Verde", "")
val gw = Country("Guiné-Bissau", "")
val stp = Country("São Tomé e Príncipe", "")
val ge = Country("Guiné Equatorial", "")

//    História Básica (easy)
//    Cultura Geral (medium)
//    Exame/Entrevista (hard)
val easyLevel = "História Básica"
val mediumLevel = "Cultura Geral"
val hardLevel = "Exame/Entrevista"

val allPacks = listOf(
    Pack(1, countryName = mz.name, countryFlag = mz.flag, title = easyLevel, difficulty = QuestionDifficulty.Easy.stringValue),
    Pack(2, countryName = mz.name, countryFlag = mz.flag, title = mediumLevel, difficulty = QuestionDifficulty.Medium.stringValue),
    Pack(3, countryName = mz.name, countryFlag = mz.flag, title = hardLevel, difficulty = QuestionDifficulty.Hard.stringValue),

    Pack(4, countryName = ao.name, countryFlag = ao.flag, title = easyLevel, difficulty = QuestionDifficulty.Easy.stringValue),
    Pack(5, countryName = ao.name, countryFlag = ao.flag, title = mediumLevel, difficulty = QuestionDifficulty.Medium.stringValue),
    Pack(6, countryName = ao.name, countryFlag = ao.flag, title = hardLevel, difficulty = QuestionDifficulty.Hard.stringValue),

    Pack(7, countryName = cv.name, countryFlag = cv.flag, title = easyLevel, difficulty = QuestionDifficulty.Easy.stringValue),
    Pack(8, countryName = cv.name, countryFlag = cv.flag, title = mediumLevel, difficulty = QuestionDifficulty.Medium.stringValue),
    Pack(9, countryName = cv.name, countryFlag = cv.flag, title = hardLevel, difficulty = QuestionDifficulty.Hard.stringValue),

    Pack(10, countryName = gw.name, countryFlag = gw.flag, title = easyLevel, difficulty = QuestionDifficulty.Easy.stringValue),
    Pack(11, countryName = gw.name, countryFlag = gw.flag, title = mediumLevel, difficulty = QuestionDifficulty.Medium.stringValue),
    Pack(12, countryName = gw.name, countryFlag = gw.flag, title = hardLevel, difficulty = QuestionDifficulty.Hard.stringValue),

    Pack(13, countryName = stp.name, countryFlag = stp.flag, title = easyLevel, difficulty = QuestionDifficulty.Easy.stringValue),
    Pack(14, countryName = stp.name, countryFlag = stp.flag, title = mediumLevel, difficulty = QuestionDifficulty.Medium.stringValue),
    Pack(15, countryName = stp.name, countryFlag = stp.flag, title = hardLevel, difficulty = QuestionDifficulty.Hard.stringValue),

    Pack(16, countryName = ge.name, countryFlag = ge.flag, title = easyLevel, difficulty = QuestionDifficulty.Easy.stringValue),
    Pack(17, countryName = ge.name, countryFlag = ge.flag, title = mediumLevel, difficulty = QuestionDifficulty.Medium.stringValue),
    Pack(18, countryName = ge.name, countryFlag = ge.flag, title = hardLevel, difficulty = QuestionDifficulty.Hard.stringValue),
)