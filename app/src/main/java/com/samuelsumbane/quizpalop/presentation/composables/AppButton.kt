package com.samuelsumbane.quizpalop.presentation.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.samuelsumbane.quizpalop.R

@Composable
fun ButtonOutlined(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    dangerMode: Boolean = false,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
        ),
        border = BorderStroke(width = 1.dp,  color = colorScheme.onBackground)
    ) {
        Text(text, color = if (dangerMode) colorScheme.onBackground else Color(0xFF010A11))
    }
}

@Composable
fun NavigateUpButton(
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick
    ) {
        BackIcon()
    }
}
