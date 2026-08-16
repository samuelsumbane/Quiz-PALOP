package org.quizpalop.app.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LoadingScreen() {
    Scaffold {
        Column(
            Modifier
                .padding(it)
                .appBackground()
                .fillMaxSize(),
            Arrangement.Center,
            Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.width(54.dp),
                color = Color(0xFF03A9F4),
                trackColor = Color.Transparent,
            )
            Spacer(Modifier.height(20.dp))
            Text("Carregando...", color = MaterialTheme.colorScheme.onBackground)
        }
    }
}