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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun RadioButtonGroup(
    optionsList: List<String>,
    lockedOptions: List<String>,
    questionCategory: QuestionCategory,
    savedQuestionsId: Triple<Set<Int>, Set<Int>, Set<Int>>,
    selectedOption: String,
    onSelect: (String) -> Unit
) {
    val backgroundColor = MaterialTheme.colorScheme.background
    val l1 = savedQuestionsId.first.containsAll(getCategoryAndLevelIds(questionCategory, "Easy"))
    val l2 = savedQuestionsId.second.containsAll(getCategoryAndLevelIds(questionCategory, "Medium))
    val l3 = savedQuestionsId.third.containsAll(getCategoryAndLevelIds(questionCategory, "Hard"))
    val categoryAndLevelValues = listOf(l1, l2, l3, false)

    Column(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(20.dp)
            .background(Color(0xBC9C9C9B), RoundedCornerShape(12.dp))
//            .blur(5.dp)
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        optionsList.forEachIndexed { index, option ->
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
                            LockedIcon()
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
                if (QuestionCategory.Bible.value !in optionsList) {
                    if (categoryAndLevelValues[index]) CheckIcon() else Text("    ")
                } else Text("")
            }
        }
    }
}


@Composable
fun CheckIcon() = Icon(Icons.Default.Check, "Check", tint = Color(0xFF09740E))
