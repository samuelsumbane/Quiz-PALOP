package com.samuelsumbane.quizpalop.presentation.composables

import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.samuelsumbane.quizpalop.presentation.maingamepage.MainGameUiState
import com.samuelsumbane.quizpalop.R


@Composable
fun GameTopStatusBar(mainGameUiState: MainGameUiState) {
    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        RowHeartAndLives(text = "${mainGameUiState.lives}")
        RowCoinAndText(text = "${mainGameUiState.userCoins}",)
    }
}

@Composable
fun RowCoinAndText(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically) {
        NormalCoinIcon()
        IconText(text, color = Color.White)
    }
}

@Composable
fun NormalCoinIcon() {
    Image(
        painter = painterResource(R.drawable.coin_2),
        contentDescription = "coin icon",
        modifier = Modifier
            .padding(10.dp)
            .size(24.dp)
    )
}
@Composable
fun RowHeartAndLives(text: String) {
    RowIconAndText(
        icon = { Icon(painter = painterResource(R.drawable.life), "", tint = Color(0xFFC6080A)) },
        text = {
            IconText(text, color = Color.White)
        }
    )
}

@Composable
fun HeartIcon(modifier: Modifier = Modifier) {
    Icon(
        painter = painterResource(R.drawable.life),
        contentDescription = "heart",
        tint = Color(0xFFC6080A),
        modifier = modifier
    )
}

@Composable
fun IconText(text: String, color: Color = Color.White) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(end = 10.dp),
        color = color
    )
}
