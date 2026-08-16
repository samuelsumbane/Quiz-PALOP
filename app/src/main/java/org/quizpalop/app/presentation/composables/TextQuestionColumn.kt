package org.quizpalop.app.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.dp

@Composable
fun TextQuestionColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val firstColor = MaterialTheme.colorScheme.background
    val secondColor = MaterialTheme.colorScheme.secondary

    Column(
        modifier = modifier
            .heightIn(min = 90.dp)
            .fillMaxWidth()
            .dropShadow(RoundedCornerShape(10.dp),
                shadow = Shadow(
                    radius = 7.dp,
                    color = if (isSystemInDarkTheme()) Color(0xC816181A) else Color(0x45111111),
                    spread = 4.dp
                ))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(firstColor, secondColor, firstColor),
                ),
                RoundedCornerShape(12.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        content()
    }
}
