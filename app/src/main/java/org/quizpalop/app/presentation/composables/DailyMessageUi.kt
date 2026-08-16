package org.quizpalop.app.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DailyChallengeMessageUi(
    modifier: Modifier,
    onButtonClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    MessageContainer(modifier = modifier) {
        content()
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            ButtonOutlined(
                text = "Fechar"
            ) { onButtonClicked() }
        }
    }
}