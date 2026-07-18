package com.samuelsumbane.quizpalop.presentation.composables

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun NotificationPermissionCard(
    state: NotificationPermissionState,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    if (state.hasPermission) return // nada a mostrar

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(0.5f))
            .zIndex(2f)
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier
//                .padding(16.dp)
                .fillMaxWidth()
                .background(Color(0xFA07111D), RoundedCornerShape(10.dp))
                .padding(10.dp)
                .align(Alignment.Center)
        ) {
            val textColor = MaterialTheme.colorScheme.background

            Text("Ativa as notificações",
                style = MaterialTheme.typography.titleMedium,
                color = textColor
            )
            Spacer(Modifier.height(8.dp))
            Text("Receber um lembrete de desafio diário", color = textColor)
            Spacer(Modifier.height(18.dp))

            Row {
                TextButton(onClick = onDismiss) {
                    Text("Agora não")
                }
                Spacer(Modifier.width(8.dp))
                Button(onClick = {
                    if (state.canAskAgain || !hasRequestedBefore(context)) {
                        state.requestPermission()
                    } else {
                        openAppSettings(context)
                    }
                }) {
                    Text("Ativar")
                }
            }
        }
    }
}

fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}