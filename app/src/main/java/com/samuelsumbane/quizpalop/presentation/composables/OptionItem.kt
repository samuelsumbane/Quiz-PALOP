package com.samuelsumbane.quizpalop.presentation.maingamepage.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.samuelsumbane.quizpalop.presentation.maingamepage.quizOptionCurrectButtonColor
import com.samuelsumbane.quizpalop.presentation.maingamepage.quizOptionWrongButtonColor
import com.samuelsumbane.quizpalop.ui.theme.HomeOptionColor

@Composable
fun OptionItem(
    prefixText: String? = null,
    text: String,
    background: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .heightIn(min = 45.dp),
        colors = ButtonDefaults.buttonColors(containerColor = background),
        shape = RoundedCornerShape(12.dp),
    ) {
        Box(
            modifier = Modifier
        ) {
            prefixText?.let {
                Text(text = it, fontWeight = FontWeight.Bold,
                    color = HomeOptionColor,
                    modifier = Modifier.align(Alignment.CenterStart),
                )
            }

            Text(
                text,
                color = if (background in listOf(quizOptionCurrectButtonColor, quizOptionWrongButtonColor)) Color.White else Color(0xFF021526),
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }
    }
}
