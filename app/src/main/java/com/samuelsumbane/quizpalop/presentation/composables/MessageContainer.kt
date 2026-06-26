package com.samuelsumbane.quizpalop.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun MessageContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .padding(15.dp)
            .background(Color(0xBC9C9C9B), RoundedCornerShape(14.dp))
            .zIndex(3f)
            .padding(10.dp, 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) { content() }
}

@Composable
fun MessageTexts(
    title: String,
    text: String,
    color: Color = Color.Black
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (title.isNotBlank()) {
            CenteredText(
                title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(0.dp, 50.dp),
                color = color
            )
        }

        CenteredText(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(0.dp, 50.dp),
            color = color
        )
    }
}

@Composable
fun CenteredText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    color: Color = Color.Black,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = text, style = style, color = color, textAlign = TextAlign.Center)
    }
}