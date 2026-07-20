package com.samuelsumbane.quizpalop.presentation.dailychallenge

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import com.samuelsumbane.quizpalop.domain.model.optionsLabels
import com.samuelsumbane.quizpalop.presentation.composables.OptionItem
import com.samuelsumbane.quizpalop.presentation.composables.QuestionText
import com.samuelsumbane.quizpalop.presentation.composables.TextQuestionColumn
import com.samuelsumbane.quizpalop.presentation.composables.appBackground
import com.samuelsumbane.quizpalop.presentation.maingamepage.GameTextMessage
import org.koin.compose.viewmodel.koinViewModel

class DailyChallengeScreen(val questionId: String) : Screen {
    @Composable
    override fun Content() {
        DailyChallenge(questionId)
    }
}

@Composable
fun DailyChallenge(questionId: String) {
    val dailyChallengeViewModel = koinViewModel<DailyChallengeViewModel>()
    val dailyChallengeUiState by dailyChallengeViewModel.dailychallengeUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        dailyChallengeViewModel.loadQuestion(questionId)
    }

    Scaffold { padding ->
        val graphicsLayer = rememberGraphicsLayer()
        Column(
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
            if (dailyChallengeUiState.gameTextMessage is GameTextMessage.Empty) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {

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
                                Text(
                                    text = dailyChallengeUiState.dailyQuestionRightAnswer,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}