package com.samuelsumbane.quizpalop.presentation.home

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.samuelsumbane.quizpalop.domain.model.PagesName
import com.samuelsumbane.quizpalop.presentation.composables.DuelIcon
import com.samuelsumbane.quizpalop.presentation.composables.FlagsComponents
import com.samuelsumbane.quizpalop.presentation.composables.GameIcon
import com.samuelsumbane.quizpalop.presentation.composables.HomeOption
import com.samuelsumbane.quizpalop.presentation.composables.HomePageOptionColumn
import com.samuelsumbane.quizpalop.presentation.composables.NotificationPermissionCard
import com.samuelsumbane.quizpalop.presentation.composables.ProgressIcon
import com.samuelsumbane.quizpalop.presentation.composables.appBackground
import com.samuelsumbane.quizpalop.presentation.composables.rememberNotificationPermissionState
import com.samuelsumbane.quizpalop.presentation.configquestions.QuestionsConfigScreen
import com.samuelsumbane.quizpalop.presentation.dailychallenge.DailyChallengeScreen
import com.samuelsumbane.quizpalop.presentation.dailychallenge.DailyChallengeViewModel
import com.samuelsumbane.quizpalop.presentation.gamesession.GameSessionScreen
import com.samuelsumbane.quizpalop.presentation.progress.ProgressPageScreen
import org.koin.androidx.compose.koinViewModel

class HomePageScreen : Screen {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Composable
    override fun Content() {
        HomePage()
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun HomePage() {
    val navigator = LocalNavigator.currentOrThrow
    val dailyChallengeViewModel = koinViewModel<DailyChallengeViewModel>()
    val dailyChallengeUiState by dailyChallengeViewModel.dailychallengeUiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val permissionState = rememberNotificationPermissionState()
    var showCard by remember { mutableStateOf(true) }


    Scaffold {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .appBackground()
        ) {
            if (showCard) {
                NotificationPermissionCard(
                    state = permissionState,
                    onDismiss = { showCard = false }
                )
            }

            Column(
                modifier = Modifier
                    .padding(5.dp, 60.dp)
                    .align(Alignment.Center)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                FlagsComponents()

                Text(
                    text = "Quiz PALOP",
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .padding(top = 30.dp)
                )

                Spacer(Modifier.height(140.dp))

                HomePageOptionColumn() {
                    HomeOption(
                        text = "Jogar",
                        aditionalElement = { GameIcon() }) {
                        navigator.push(GameSessionScreen())
                    }

//                    dailyChallengeUiState.dailyQuestionId?.let { questionId ->
                        HomeOption(
                            text = "Desafio diário",
                            aditionalElement = {},
                            onClick = { navigator.push(DailyChallengeScreen("mz_02"))}
                        )
//                    }

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
                text = "Versão: 1.0.0",
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