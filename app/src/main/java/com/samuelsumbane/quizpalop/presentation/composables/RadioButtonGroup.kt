package com.samuelsumbane.quizpalop.presentation.composables

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.samuelsumbane.quizpalop.R
import com.samuelsumbane.quizpalop.domain.model.Category
import com.samuelsumbane.quizpalop.domain.model.Countries
import com.samuelsumbane.quizpalop.domain.model.Question

@Composable
fun RadioButtonGroup(
    allQuestions: List<Question>,
    optionsList: List<String>,
    lockedOptions: List<String>,
    questionsCountry: Countries,
    savedQuestionsId: Triple<Set<String>, Set<String>, Set<String>>,
    selectedOption: String,
    onSelect: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(20.dp)
            .background(Color(0xCB9C9C9B), RoundedCornerShape(12.dp))
//            .blur(5.dp)
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        optionsList.forEachIndexed { index, option ->
            println("pais: o index é $index")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { if (option !in lockedOptions) onSelect(option) },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (lockedOptions.isNotEmpty()) {
                        if (option in lockedOptions) {
                            LockIcon()
                        } else {
                            Text("     ")
                        }
                    } else {
                        Text("")
                    }

                    RadioButton(
                        selected = option == selectedOption,
                        onClick = { if (option !in lockedOptions) onSelect(option) },
                        colors = RadioButtonDefaults.colors(
                            unselectedColor = Color.Black.copy(alpha = 0.5f),
                            selectedColor = Color(0xFF0B3052)
                        ),
                        enabled = option !in lockedOptions
                    )
                }
                Text(option, color = Color.Black, fontWeight = FontWeight.SemiBold)
                // Check if all category and level questions are answered
                if (optionsList.size == 3) {
                    val l1 = savedQuestionsId.first.containsAll(allQuestions.filter { it.questionLevel == "Easy" }.map { it.id })
                    val l2 = savedQuestionsId.second.containsAll(allQuestions.filter { it.questionLevel == "Medium" }.map { it.id })
                    val l3 = savedQuestionsId.third.containsAll(allQuestions.filter { it.questionLevel == "Hard" }.map { it.id })
                    val categoryAndLevelValues = listOf(l1, l2, l3, false)

                    if (Category.History.categoryName !in optionsList) {
                        if (categoryAndLevelValues[index]) CheckIcon() else Text("    ")
                    } else Text("")
                }
            }
        }
    }
}


@Composable
fun CheckIcon() = Icon(painterResource(R.drawable.door_open_fill), "", tint = Color(0xFF09740E))
