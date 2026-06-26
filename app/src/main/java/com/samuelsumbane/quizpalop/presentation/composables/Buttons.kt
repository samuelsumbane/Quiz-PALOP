package com.samuelsumbane.quizpalop.presentation.composables

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun AppButton(
    text: String,
    modifier: Modifier = Modifier,
    minText: Boolean = false,
    dangerMode: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val typo = MaterialTheme.typography
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (dangerMode) Color(0xFFEA4242) else Color(0xFF1075BF),
            contentColor = Color.White
        )
    ) { Text(text, style = if (minText) typo.bodySmall else typo.bodySmall) }
}