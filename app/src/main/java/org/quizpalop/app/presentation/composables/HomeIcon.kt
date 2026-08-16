package org.quizpalop.app.presentation.composables

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun HomeIcon(painter: Painter, description: String, color: Color = Color(0x0F062232)) {
    Icon(
        painter = painter,
        contentDescription = description,
        modifier = Modifier.size(24.dp),
        tint = color
    )
}