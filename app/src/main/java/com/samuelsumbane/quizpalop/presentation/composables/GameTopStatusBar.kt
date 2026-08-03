package com.samuelsumbane.quizpalop.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.samuelsumbane.quizpalop.R
import com.samuelsumbane.quizpalop.presentation.maingamepage.MainGameUiEvents
import com.samuelsumbane.quizpalop.presentation.maingamepage.MainGameUiState
import com.samuelsumbane.quizpalop.presentation.maingamepage.MainGameViewModel
import com.samuelsumbane.quizpalop.presentation.maingamepage.SoundState


@Composable
fun GameTopStatusBar(
    mainGameViewModel: MainGameViewModel,
    mainGameUiState: MainGameUiState,
    modifier: Modifier,
) {
    Box(
        modifier = modifier
//            .padding(10.dp)
//            .background(Color.Red)
            .fillMaxWidth(),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .background(MaterialTheme.colorScheme.primary.copy(0.4f), RoundedCornerShape(12.dp))
                .align(Alignment.TopStart)
            ,
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RowHeartAndLives(text = "${mainGameUiState.lives}")
            RowQuestionTimer(mainGameUiState.questionsTimer)
            RowCoinAndText(text = "${mainGameUiState.userCoins}",)
        }

        IconButton(
            onClick = { mainGameViewModel.onEvent(MainGameUiEvents.OnToggleShowConfig) },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(painterResource(R.drawable.more), "", modifier = Modifier.size(24.dp))
        }

        if (mainGameUiState.showGameConfigs) {
            Column(
                modifier = Modifier
                    .padding(top = 45.dp)
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
                    .align(Alignment.TopEnd)
            ) {
                val iconColor = MaterialTheme.colorScheme.onPrimary
                IconButton(
                    onClick = { mainGameViewModel.toogleSoundState(mainGameUiState.soundState != SoundState.Playing) }
                ) {
                    if (mainGameUiState.soundState != SoundState.Playing) {
                        HomeIcon(painterResource(R.drawable.volume_up_fill), "Volume up", iconColor)
                    } else {
                        HomeIcon(
                            painterResource(R.drawable.volume_mute_fill),
                            "Mute volume",
                            iconColor
                        )
                    }
                }

                IconButton(
                    onClick = { mainGameViewModel.onEvent(MainGameUiEvents.OnToggleHapticState) }
                ) {
                    if (mainGameUiState.mobileVibrate) VibrationOff() else VibrateIcon()
                }
            }
        }

    }
}

@Composable
fun RowCoinAndText(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically) {
        NormalCoinIcon()
        IconText(text, color = MaterialTheme.colorScheme.onPrimary)
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
            IconText(text, color = MaterialTheme.colorScheme.onPrimary)
        }
    )
}
@Composable
fun RowQuestionTimer(time: Int) {
    val elementColor = if (time > 10) MaterialTheme.colorScheme.onPrimary else Color.Red
    RowIconAndText(
        icon = { Icon(painter = painterResource(R.drawable.clock), "", tint = elementColor) },
        text = {
            IconText("$time", color = elementColor)
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
