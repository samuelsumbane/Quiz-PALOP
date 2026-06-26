package com.samuelsumbane.quizpalop.presentation.maingamepage

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import com.samuelsumbane.quizpalop.domain.model.AdState
import com.samuelsumbane.quizpalop.domain.model.ChangeCountValues
import com.samuelsumbane.quizpalop.domain.repository.RewardedAdManager
import com.samuelsumbane.quizpalop.presentation.composables.AppButton
import com.samuelsumbane.quizpalop.presentation.composables.CenteredText
import com.samuelsumbane.quizpalop.presentation.composables.HorizontalDividerWithText
import com.samuelsumbane.quizpalop.presentation.composables.RowCoinAndText
import com.samuelsumbane.quizpalop.presentation.composables.RowHeartAndLives
import com.samuelsumbane.quizpalop.presentation.composables.VerticallyCenteredRowAndSpacedBetween
import com.samuelsumbane.quizpalop.presentation.composables.verticallyCenteredRowContent
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun NoMoreLivesUI(
    navigator: Navigator,
    gameQuizViewModel: MainGameViewModel,
    quizGameUiState: MainGameUiState,
    manager: RewardedAdManager,
    activity: Activity,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .background(Color(0xBC9C9C9B), RoundedCornerShape(12.dp))
            .padding(10.dp, 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Você ficou sem vidas", color = Color.Black, style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(50.dp))
        HorizontalDividerWithText("Comprar vidas")
        Text(
            "Suas moedas: ${quizGameUiState.userCoins}",
            modifier = Modifier.padding(0.dp, 10.dp),
            color = Color.Black
        )
        Column(
            modifier = Modifier
                .padding(top = 10.dp, bottom = 20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            VerticallyCenteredRowAndSpacedBetween(
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                verticallyCenteredRowContent {
                    RowHeartAndLives("1")
                    Text(" por   ", color = Color.Black)
                    RowCoinAndText("25")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AppButton(
                        text = "Comprar",
                        enabled = quizGameUiState.userCoins >= 25
                    ) {
                        gameQuizViewModel.buyLifeWithCoins(25, 1)
                    }


                    if (quizGameUiState.userCoins < 25) {
                        Text("Moedas insuficientes", style = MaterialTheme.typography.bodySmall, color = Color.Black)
                    }
                }
            }
            VerticallyCenteredRowAndSpacedBetween(
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                verticallyCenteredRowContent {
                    RowHeartAndLives("3")
                    Text(" por   ", color = Color.Black)
                    RowCoinAndText("70")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AppButton(
                        text = "Comprar",
                        enabled = quizGameUiState.userCoins >= 70
                    ) {
                        gameQuizViewModel.buyLifeWithCoins(70, 3)
                    }

                    if (quizGameUiState.userCoins < 70) Text("Moedas insuficientes", style = MaterialTheme.typography.bodySmall, color = Color.Black)

                }
            }
//                        HorizontalDivider(Modifier.fillMaxWidth())

            VerticallyCenteredRowAndSpacedBetween(
                modifier = Modifier.padding(0.dp, 15.dp)
            ) {
                verticallyCenteredRowContent {
                    RowHeartAndLives("1")
                    Text(" por  ", color = Color.Black)
                    verticallyCenteredRowContent {
                        Text("1 ", style = MaterialTheme.typography.titleSmall, color = Color.White)
                        Text("anúncio", color = Color.Black)
                    }
                }

                AppButton(
                    text = quizGameUiState.adState.stateName,
                    modifier = Modifier.padding(10.dp),
                    minText = true
                ) {
                    when (quizGameUiState.adState) {
                        AdState.Ready -> {
                            manager.show(activity) {
                                gameQuizViewModel.changeLivesCount(ChangeCountValues.IncreaseLives(1))
                                gameQuizViewModel.setGameTextMessage(GameTextMessage.NewLifeEarned("Parabéns, ganhou +1 vida."))
                            }
                        }
                        AdState.Loading, AdState.Error  -> gameQuizViewModel.loadAd(manager)
                    }
                }
            }

            val currentTime = System.currentTimeMillis()
            val passedTime = (currentTime - quizGameUiState.lastDateTimeLostLives).milliseconds
            val hours = passedTime.inWholeHours
            if (quizGameUiState.lastDateTimeLostLives > 0L) {
                when {
                    hours < 2 && quizGameUiState.lives == 0 -> {
                        val remaingTime = 2.hours - passedTime
                        HorizontalDividerWithText("Ganhar vida")
                        Column(modifier = Modifier.padding(0.dp, 10.dp)) {
                            CenteredText("Ganhará uma vida daqui a ${remaingTime.inWholeHours}h ${remaingTime.inWholeMinutes % 60}m.")
                        }
                    }
                    hours >= 2 && quizGameUiState.lives == 0 -> {
                        gameQuizViewModel.changeLivesCount(ChangeCountValues.IncreaseLives(1))
                        gameQuizViewModel.clearLastDateTimeLostLives()
                        gameQuizViewModel.setGameTextMessage(GameTextMessage.NewLifeEarned("Parabéns! Recebeu uma nova vida"))
                    }
                }
            }
        }

        HorizontalDivider()

        Row(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                colors = ButtonDefaults
                    .buttonColors(
                        quizOptionWrongButtonColor,
                        Color.White
                    ),
                onClick = {
                    gameQuizViewModel.clearAnsweredQuestionsWithoutMistake()
                    gameQuizViewModel.setGameTextMessage(GameTextMessage.Empty)
//                    navigator.push(HomeGameScreen())
                }
            ) {
                Text("Sair do jogo")
            }
        }
    }
}