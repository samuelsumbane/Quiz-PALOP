package org.quizpalop.app.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.quizpalop.app.presentation.maingamepage.quizOptionCorrectButtonColor
import org.quizpalop.app.presentation.maingamepage.quizOptionWrongButtonColor

@Composable
fun OptionItem(
    text: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    prefixText: Char? = null,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .heightIn(min = 45.dp)
            .background(backgroundColor, RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        prefixText?.toString()?.let {
            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .align(Alignment.CenterStart)
                    .background(colorScheme.onBackground, RoundedCornerShape(25))
                    .padding(4.dp)
            ) {
                Text(text = it, fontWeight = FontWeight.ExtraBold,
                    color = colorScheme.background,
                    fontSize = 25.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .size(30.dp)
                )
            }

        }

        Text(
            text,
            color = if (backgroundColor in listOf(quizOptionCorrectButtonColor, quizOptionWrongButtonColor)) Color.White else Color(0xFF021526),
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth(0.72f)
                .align(Alignment.Center)
        )
    }
}
