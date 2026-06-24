package com.samuelsumbane.quizpalop.presentation.maingamepage.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun QuestionText(text: String, modifierFontSize: Boolean) {
    Text(
        text = text,
        fontWeight = FontWeight.ExtraBold,
        fontSize = if (modifierFontSize) text.toFontSize().sp else 24.sp,
        textAlign = TextAlign.Center,
        color = Color.White,
        lineHeight = 34.sp,
        modifier = Modifier.padding(5.dp)
    )
}

fun String.toFontSize() = if (this.length > 30) 17 else 24