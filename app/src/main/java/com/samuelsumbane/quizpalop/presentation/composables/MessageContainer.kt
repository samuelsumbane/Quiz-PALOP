package com.samuelsumbane.quizpalop.presentation.composables

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.navigator.Navigator
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.samuelsumbane.quizpalop.domain.model.AdState
import com.samuelsumbane.quizpalop.domain.model.HelpOption
import com.samuelsumbane.quizpalop.domain.repository.RewardedAdManager
import com.samuelsumbane.quizpalop.presentation.home.HomePageScreen
import com.samuelsumbane.quizpalop.presentation.maingamepage.GameTextMessage
import com.samuelsumbane.quizpalop.presentation.maingamepage.MainGameUiState
import com.samuelsumbane.quizpalop.presentation.maingamepage.MainGameViewModel
import com.samuelsumbane.quizpalop.presentation.maingamepage.clearAnsweredQuestionsWithoutMistake
import com.samuelsumbane.quizpalop.presentation.maingamepage.onCloseMessageModal
import com.samuelsumbane.quizpalop.presentation.maingamepage.setGameTextMessage
import com.samuelsumbane.quizpalop.presentation.maingamepage.showCurrectOptionAfterViewAd

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

@Composable
fun MessageUi(
    navigator: Navigator,
    mainGameViewModel: MainGameViewModel,
    mainPageUiState: MainGameUiState,
    activity: Activity,
    manager: RewardedAdManager,
) {
    val coinsIcon by rememberLottieComposition(
        LottieCompositionSpec.Asset("lottie/coins_ani.lottie")
    )
    val heartIcon by rememberLottieComposition(
        LottieCompositionSpec.Asset("lottie/heart.lottie")
    )

    val brokenHeartIcon by rememberLottieComposition(
        LottieCompositionSpec.Asset("lottie/broken_heart.lottie")
    )

    val successIcon by rememberLottieComposition(
        LottieCompositionSpec.Asset("lottie/success.lottie")
    )

    if (mainPageUiState.gameTextMessage !is GameTextMessage.Empty) {
        var helpOption: HelpOption? = null

        MessageContainer {
            when (val message = mainPageUiState.gameTextMessage) {
                is GameTextMessage.AddedCoins -> {
                    LoadAnimatedIcons(coinsIcon, Modifier.size(220.dp))
                    MessageTexts(title = "", message.message, )
                }

                is GameTextMessage.ShowRightAnswer -> {
                    LoadAnimatedIcons(successIcon)
                    MessageTexts(title = "", message.message)
                }

                is GameTextMessage.CannotGetHelp -> {
//                    LoadAnimatedIcons(sadIcon)
                    MessageTexts(message.reasonTitle, message.reasonMessage)
                    if (message.helpOption == HelpOption.RightOption) {
                        helpOption = HelpOption.RightOption
                        TwoButtonsRow(
                            text = "Deseja ver um anúncio para obter a resposta correcta?",
                            outlinedText = "Não, obrigado!",
                            outlinedClicked = { mainGameViewModel.onCloseMessageModal() },
                            filledButtonText = mainPageUiState.adState.stateName,
                            onFilledButtonClicked = {
                                when (mainPageUiState.adState) {
                                    AdState.Ready -> {
                                        manager.show(activity) {
                                            mainGameViewModel.showCurrectOptionAfterViewAd()
                                        }
                                    }
                                    AdState.Loading, AdState.Error  -> mainGameViewModel.loadAd(manager)
                                }
                            }
                        )
                    } else helpOption = HelpOption.FiftFift

                }

                is GameTextMessage.QuestionNotAnswered -> {
                    LoadAnimatedIcons(brokenHeartIcon)
                    MessageTexts(message.title, message.message)
                }

                is GameTextMessage.ExitGame -> {
                    BigExitAppIcon()
                    TwoButtonsRow(
                        text = message.confirmationText,
                        outlinedText = "Cancelar",
                        outlinedClicked = { mainGameViewModel.onCloseMessageModal() },
                        filledButtonText = "Sair",
                        dangerMode = true,
                        onFilledButtonClicked = {
                            mainGameViewModel.clearAnsweredQuestionsWithoutMistake()
                            mainGameViewModel.setGameTextMessage(GameTextMessage.Empty)
                            navigator.push(HomePageScreen())
                        }
                    )
                }

                is GameTextMessage.NewLifeEarned -> {
//                    HeartIcon(modifier = Modifier.size(80.dp, 100.dp))
                    LoadAnimatedIcons(heartIcon)
                    MessageTexts(title = "", message.message)
                    AppButton(text = "Continuar com o jogo") { mainGameViewModel.startLoadingNextQuestion() }
                }


                else -> { Text("") }
            }

            if (mainPageUiState.gameTextMessage !is GameTextMessage.ExitGame &&
                mainPageUiState.gameTextMessage !is GameTextMessage.SelectedQuestionsAnswered &&
                mainPageUiState.gameTextMessage !is GameTextMessage.AllQuestionsAnswered &&
                mainPageUiState.gameTextMessage !is GameTextMessage.NewLifeEarned &&
                mainPageUiState.gameTextMessage !is GameTextMessage.AddedCoins &&
                helpOption != HelpOption.RightOption
            ) {
                ButtonOutlined(
                    text = "Fechar",
                    modifier = Modifier
                        .padding(top = 12.dp)
                ) { mainGameViewModel.onCloseMessageModal() }
            } else Text("")
        }
    }

}