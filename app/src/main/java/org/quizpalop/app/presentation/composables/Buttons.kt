package org.quizpalop.app.presentation.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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

@Composable
fun AppTextButton(onDismiss: () -> Unit) {
    TextButton(
        onClick = onDismiss,
    ) {
        Text("Agora não", color = Color.White)
    }
}