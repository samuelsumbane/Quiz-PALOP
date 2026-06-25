package com.samuelsumbane.quizpalop.presentation.maingamepage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import com.samuelsumbane.quizpalop.presentation.composables.GameBottomButton
import com.samuelsumbane.quizpalop.presentation.composables.IconData
import com.samuelsumbane.quizpalop.presentation.composables.LoadingScreen
import com.samuelsumbane.quizpalop.presentation.maingamepage.composables.OptionItem
import com.samuelsumbane.quizpalop.presentation.composables.QuestionText
import com.samuelsumbane.quizpalop.presentation.maingamepage.composables.TextQuestionColumn
import org.koin.androidx.compose.koinViewModel
import com.samuelsumbane.quizpalop.R
import com.samuelsumbane.quizpalop.presentation.composables.GameTopStatusBar


class MainPageScreen : Screen {
    @Composable
    override fun Content() {
        MainPage()
    }
}

@Composable
fun MainPage() {
    val mainPageViewModel = koinViewModel<MainGameViewModel>()
    val mainPageUiState by mainPageViewModel.mainGameUiState.collectAsStateWithLifecycle()

    @Composable
    fun pageContent() {
        mainPageUiState.actualQuestion?.let { questionData ->
            Scaffold(
                bottomBar = {
                    AnimatedVisibility(
                        visible = true,
//                        visible = mainPageUiState.gameTextMessage is GameTextMessage.Empty && mainPageUiState.lives > 0,
                        enter = slideInHorizontally(
                            initialOffsetX = { -it }
                        ) + fadeIn(),
                        exit = slideOutHorizontally(
                            targetOffsetX = { it }
                        ) + fadeOut()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
//                        .background(Color.DarkGray.copy(0.5f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .background(Color(0x859C9C9B), RoundedCornerShape(12.dp))
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                GameBottomButton(
                                    icon = IconData(R.drawable.fift_fift_icon, "fift fift"),
                                    buttonText = "50 50",
                                    requiredCoins = 15,
                                ) {
//                                gameQuizViewModel.onEvent(MainPageUiEvents.OnHelp(HelpOption.FiftFift, questionData.id))
                                }

                                GameBottomButton(
                                    icon = IconData(R.drawable.extra_time, "Ask more time"),
                                    buttonText = "+ Tempo",
//                                    requiredCoins = questionData.level.requiredCoinsForAditionalTime,
                                    requiredCoins = 15,
                                ) {
//                                    gameQuizViewModel.onEvent(
//                                        MainPageUiEvents.OnHelp(
//                                            HelpOption.MoreTime,
//                                            questionData.id
//                                        )
//                                    )
                                }

                                Row(
                                    modifier = Modifier.width(45.dp),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    GameBottomButton(
                                        icon = IconData(R.drawable.door_open_fill, "Exit"),
                                        requiredCoins = 0,
                                        buttonText = "Sair",
                                    ) {
//                                        gameQuizViewModel.onEvent(MainPageUiEvents.OnExit)
                                    }
                                }
                            }
                        }
                    }
                }
            ) { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    GameTopStatusBar(mainPageUiState)

                    Column(
                        modifier = Modifier
                            .weight(1f),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {

                        TextQuestionColumn(
                            modifier = Modifier.padding(10.dp)
                        ) {
                            mainPageUiState.actualQuestion?.let { question ->
                                QuestionText(question.question, modifierFontSize = false)
                            }
                        }

                        mainPageUiState.actualQuestion?.let { question ->
                            LazyColumn(
                            ) {
                                items(1) {
                                    question.options.forEachIndexed { index, option ->
                                        OptionItem(
                                            text = option,
                                            background = mainPageUiState.optionsColors[index]
                                        ) {
                                            mainPageViewModel.onEvent(MainGameUiEvents.OnCheckResponse(option))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }


    when (mainPageUiState.pageState) {
        MainPageState.Loading -> LoadingScreen()
        MainPageState.DisplayContent -> pageContent()
    }

}