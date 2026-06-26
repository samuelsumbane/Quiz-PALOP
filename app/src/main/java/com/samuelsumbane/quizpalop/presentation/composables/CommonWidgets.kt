package com.samuelsumbane.quizpalop.presentation.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun HorizontalDividerWithText(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("$text ", color = Color.Black, fontWeight = FontWeight.SemiBold)
        HorizontalDivider(Modifier.fillMaxWidth())
    }
}