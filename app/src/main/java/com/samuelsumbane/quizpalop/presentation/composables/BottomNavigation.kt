package com.samuelsumbane.quizpalop.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavigation(
    backButtonEnabled: Boolean = true,
    onBackButtonClicked: () -> Unit,
    onForwardButtonClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 40.dp, end = 40.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        ConfigsNavigationIcon(icon = { BackIcon() }) { onBackButtonClicked() }

        if (backButtonEnabled) {
            Button(
                onClick = onForwardButtonClicked,
                modifier = Modifier
                    .width(110.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor =  Color(0xFF0B8BE3),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(20)
            ) {
                Text("Começar")
            }
        } else {
            ConfigsNavigationIcon(icon = { ForwardIcon() }) { onForwardButtonClicked() }
        }
    }
}