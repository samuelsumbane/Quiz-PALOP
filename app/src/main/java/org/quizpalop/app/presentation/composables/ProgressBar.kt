package org.quizpalop.app.presentation.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.quizpalop.app.domain.model.QuestionLevel

@Composable
fun ProgressBar(
//    text: String,
    actualPercentage: Float,
    level: QuestionLevel? = null
) {
    val progressbarColor = when (level) {
        QuestionLevel.Easy -> Color(0xFF4CAF50)
        QuestionLevel.Medium -> Color(0xFFFFC107)
        QuestionLevel.Hard -> Color(0xFFF8493C)
        else -> Color(0xFF2196F3)
    }
    val formattedPercentage = (actualPercentage * 100).toString().take(4).toFloat()

    Column(
        modifier = Modifier
            .padding(start = 15.dp, top = 30.dp, end = 15.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(15.dp, 20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = level?.levelName ?: "Todo", color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.SemiBold)

                CircularProgressIndicator(
                    progress = { actualPercentage },
                    modifier = Modifier
                        .padding(15.dp)
                        .size(120.dp),
                    color = progressbarColor,
                    strokeWidth = 8.dp,
                    trackColor = Color.DarkGray,
                    gapSize = 0.dp
                )

                Text(
                    text = "$formattedPercentage% Completo",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
        }
    }
}
