package com.samuelsumbane.quizpalop.presentation.dailychallenge

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.samuelsumbane.quizpalop.domain.model.optionsLabels
import com.samuelsumbane.quizpalop.presentation.composables.DailyChallengeMessageUi
import com.samuelsumbane.quizpalop.presentation.composables.HomeIcon
import com.samuelsumbane.quizpalop.presentation.composables.IconAndTextColumn
import com.samuelsumbane.quizpalop.presentation.composables.LoadingScreen
import com.samuelsumbane.quizpalop.presentation.composables.MessageTexts
import com.samuelsumbane.quizpalop.presentation.composables.OptionItem
import com.samuelsumbane.quizpalop.presentation.composables.PageUiState
import com.samuelsumbane.quizpalop.presentation.composables.PrintScreenIcon
import com.samuelsumbane.quizpalop.presentation.composables.QuestionText
import com.samuelsumbane.quizpalop.presentation.composables.TextQuestionColumn
import com.samuelsumbane.quizpalop.presentation.composables.appBackground
import com.samuelsumbane.quizpalop.presentation.home.HomePageScreen
import com.samuelsumbane.quizpalop.presentation.maingamepage.quizOptionCurrectButtonColor
import com.samuelsumbane.quizpalop.ui.theme.HomeOptionColor
import org.koin.compose.viewmodel.koinViewModel

class DailyChallengeScreen(val questionId: String) : Screen {
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    override fun Content() {
        DailyChallenge(questionId)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DailyChallenge(questionId: String) {
    val dailyChallengeViewModel = koinViewModel<DailyChallengeViewModel>()
    val dailyChallengeUiState by dailyChallengeViewModel.dailychallengeUiState.collectAsStateWithLifecycle()
    val navigator = LocalNavigator.currentOrThrow

    LaunchedEffect(Unit) {
        dailyChallengeViewModel.loadQuestion(questionId)
    }

    Scaffold { padding ->
        val graphicsLayer = rememberGraphicsLayer()
        val context = LocalContext.current

        when (dailyChallengeUiState.pageUiState) {
            PageUiState.DisplayContent -> Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .appBackground()
                    .padding(10.dp)
                    .drawWithContent {
                        graphicsLayer.record {
                            this@drawWithContent.drawContent()
                        }
                        drawLayer(graphicsLayer)
                    },
            ) {
                when (val message = dailyChallengeUiState.dailyChallengeMessage) {
                    is DailyChallengeMessage.RightAnswer -> {
                         DailyChallengeMessageUi(
                             modifier = Modifier.align(Alignment.Center),
                             onButtonClicked = { dailyChallengeViewModel.onEvent(
                                 DailyChallengeUiEvents.OnCloseMessageContainer) }
                         ) {
                            MessageTexts(message.title, message.message)
                            AwardText(" moedas ter respondido correctamete", message.earnedCoins)
                        }
                    }

                    is DailyChallengeMessage.WrongAnswer -> {
                        DailyChallengeMessageUi(
                            modifier = Modifier.align(Alignment.Center),
                            onButtonClicked = { dailyChallengeViewModel.onEvent(
                                DailyChallengeUiEvents.OnCloseMessageContainer) }
                        ) {
                            MessageTexts(message.title, message.message)
                            RightAnswerQuestionText("A resposta correcta é: ",
                                message.rightAnswerText
                            )
                            AwardText(" moeda pela participação",message.earnedCoins)
                        }
                    }

                    DailyChallengeMessage.Empty -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.92f),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(start = 10.dp)
                            ) {
                                Text("País:             ${dailyChallengeUiState.questionCountry.countryName}")
                                Text("Categoria:   ${dailyChallengeUiState.questionCategory.categoryName}")
                            }

                            TextQuestionColumn(
                                modifier = Modifier.padding(10.dp)
                            ) {
                                dailyChallengeUiState.dailyQuestion?.let { question ->
                                    QuestionText(
                                        question.question,
                                        modifierFontSize = false
                                    )
                                }
                            }

                            dailyChallengeUiState.dailyQuestion?.let { question ->
                                LazyColumn {
                                    items(1) {
                                        question.options.forEachIndexed { index, option ->
                                            OptionItem(
                                                prefixText = optionsLabels[index],
                                                text = option,
                                                backgroundColor = dailyChallengeUiState.optionsColors[index]
                                            ) {
                                                dailyChallengeViewModel.onEvent(
                                                    DailyChallengeUiEvents.OnCheckResponse(option)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            Text("")
                        }

                        if (dailyChallengeUiState.showBottomBar) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                IconAndTextColumn(
                                    text = "Casa",
                                    onClick = { navigator.push(HomePageScreen())}
                                ) { HomeIcon(tint = HomeOptionColor) }

                                IconAndTextColumn(
                                    text = "Captura de tela",
                                    onClick = {
                                       dailyChallengeViewModel.onEvent(
                                           DailyChallengeUiEvents.OnPrintScree(context, graphicsLayer)
                                       )
                                    },
                                    enabled = quizOptionCurrectButtonColor in dailyChallengeUiState.optionsColors
                                ) { PrintScreenIcon(tint = HomeOptionColor) }
                            }
                        }
                    }
                }
            }
            else -> LoadingScreen()
        }
    }
}

@Composable
fun AwardText(
    text: String,
    coinsText: String
) {
    Row(
        modifier = Modifier
            .padding(0.dp, 15.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            buildAnnotatedString {
                append("Ganhou ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)) {
                    append(coinsText)
                }
                append(text)
            },
            color = Color.Black,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun RightAnswerQuestionText(
    normalText: String,
    importantText: String
) {
   Row(
       modifier = Modifier
           .padding(0.dp, 15.dp)
           .fillMaxWidth(),
       horizontalArrangement = Arrangement.Center
   ) {
       Text(
           buildAnnotatedString {
               append(normalText)
               withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Color(0xFF066106))) {
                   append(importantText)
               }
           },
           color = Color.Black
       )
   }
}