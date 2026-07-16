package com.samuelsumbane.quizpalop.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
        primary = Color(68, 68, 68),
         onPrimary = Color.White,
        secondary = Color(0xFF041B28),
        tertiary = Color(0xCB9C9C9B)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(224, 224,224),
    onPrimary = Color(18, 18, 18),
    secondary = Color(0x704CAEF8),
    tertiary = Color(0xFFB6B8B8)
)

@Composable
fun QuizPALOPTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}