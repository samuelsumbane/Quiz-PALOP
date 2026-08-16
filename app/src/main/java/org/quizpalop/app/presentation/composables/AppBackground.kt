package org.quizpalop.app.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun Modifier.appBackground(): Modifier {
    return this
        .background(
            brush = Brush.linearGradient(
                colors = listOf(Color.Transparent, MaterialTheme.colorScheme.primary.copy(alpha = 0.8f), Color.Transparent),
            )
        )
}