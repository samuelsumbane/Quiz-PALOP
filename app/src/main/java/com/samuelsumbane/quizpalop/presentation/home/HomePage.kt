package com.samuelsumbane.quizpalop.presentation.home

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.samuelsumbane.quizpalop.R
import com.samuelsumbane.quizpalop.core.notifications.AlarmScheduler
import com.samuelsumbane.quizpalop.domain.model.PagesName
import com.samuelsumbane.quizpalop.presentation.composables.DailyChallengeIcon
import com.samuelsumbane.quizpalop.presentation.composables.DuelIcon
import com.samuelsumbane.quizpalop.presentation.composables.FlagsComponents
import com.samuelsumbane.quizpalop.presentation.composables.GameIcon
import com.samuelsumbane.quizpalop.presentation.composables.HomeOption
import com.samuelsumbane.quizpalop.presentation.composables.HomePageOptionColumn
import com.samuelsumbane.quizpalop.presentation.composables.NotificationPermissionRequester
import com.samuelsumbane.quizpalop.presentation.composables.ProgressIcon
import com.samuelsumbane.quizpalop.presentation.composables.appBackground
import com.samuelsumbane.quizpalop.presentation.configquestions.QuestionsConfigScreen
import com.samuelsumbane.quizpalop.presentation.configquestions.QuestionsConfigViewModel
import com.samuelsumbane.quizpalop.presentation.dailychallenge.DailyChallengeLoadQuestionState
import com.samuelsumbane.quizpalop.presentation.dailychallenge.DailyChallengeScreen
import com.samuelsumbane.quizpalop.presentation.dailychallenge.DailyChallengeViewModel
import com.samuelsumbane.quizpalop.presentation.gamesession.GameSessionScreen
import com.samuelsumbane.quizpalop.presentation.progress.ProgressPageScreen
import com.samuelsumbane.quizpalop.presentation.settings.SettingsScreen
import org.koin.androidx.compose.koinViewModel
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class HomePageScreen : Screen {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Composable
    override fun Content() {
        HomePage()
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun HomePage() {
    val navigator = LocalNavigator.currentOrThrow
    val dailyChallengeViewModel = koinViewModel<DailyChallengeViewModel>()
    val dailyChallengeUiState by dailyChallengeViewModel.dailychallengeUiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val configQuestionsViewModel = koinViewModel<QuestionsConfigViewModel>()
    val configQuestionsUiState by configQuestionsViewModel.questionsConfigUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        dailyChallengeViewModel.resetState()
        dailyChallengeViewModel.getAllSavedDailyQuestions()
    }

    Scaffold {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .appBackground()
        ) {
            NotificationPermissionRequester()

            IconButton(
                onClick = { navigator.push(SettingsScreen())},
                modifier = Modifier
                    .padding(top = 10.dp, end = 10.dp)
                    .align(Alignment.TopEnd)
            ) {
                Icon(painterResource(R.drawable.gear), "", modifier = Modifier.size(24.dp))
            }

            Column(
                modifier = Modifier
                    .padding(5.dp, 60.dp)
                    .align(Alignment.Center)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Quiz PALOP",
                    fontSize = 45.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .padding(0.dp,20.dp)
                )

                FlagsComponents()

                Spacer(Modifier.height(140.dp))

                HomePageOptionColumn() {
                    HomeOption(
                        text = "Jogar",
                        aditionalElement = { GameIcon() }) {
                        navigator.push(GameSessionScreen())
                    }

                    when (dailyChallengeUiState.dailyChallengeLoadQuestionState) {
                        DailyChallengeLoadQuestionState.LOADING -> {}
                        DailyChallengeLoadQuestionState.FINISHED -> {
                            configQuestionsViewModel.readSavedCountryAndCategory(false)
                            if (configQuestionsUiState.lastCategoryWasSaved) {
                                dailyChallengeUiState.dailyQuestionId?.let { questionId ->
                                    HomeOption(
                                        text = "Desafio diário",
                                        aditionalElement = { DailyChallengeIcon() },
                                        onClick = { navigator.push(DailyChallengeScreen(questionId)) }
                                    )
                                }
                            }
                        }
                    }

                    HomeOption(
                        "Dois jogadores",
                        aditionalElement = { DuelIcon() }) {
                        navigator.push(
                            QuestionsConfigScreen(PagesName.DuelPage)
                        )
                    }
                    HomeOption(
                        "Progresso",
                        aditionalElement = { ProgressIcon() }) {
                        navigator.push(
                            ProgressPageScreen()
                        )
                    }
                }
            }

            Text(
                text = "Versão: 1.0.1",
                color = Color.Gray,
                fontSize = 10.sp,
                modifier = Modifier
                    .padding(10.dp)
                    .background(MaterialTheme.colorScheme.background, RoundedCornerShape(7.dp))
                    .padding(5.dp)
                    .align(Alignment.BottomEnd)
                )
        }
    }
}