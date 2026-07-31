package com.samuelsumbane.quizpalop.presentation.gamesession

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.samuelsumbane.quizpalop.domain.model.PagesName
import com.samuelsumbane.quizpalop.presentation.composables.BackIcon
import com.samuelsumbane.quizpalop.presentation.composables.HomeOption
import com.samuelsumbane.quizpalop.presentation.composables.HomePageOptionColumn
import com.samuelsumbane.quizpalop.presentation.composables.LoadingScreen
import com.samuelsumbane.quizpalop.presentation.composables.PageLayout
import com.samuelsumbane.quizpalop.presentation.composables.PageTitleText
import com.samuelsumbane.quizpalop.presentation.configquestions.QuestionsConfigPageUiState
import com.samuelsumbane.quizpalop.presentation.configquestions.QuestionsConfigScreen
import com.samuelsumbane.quizpalop.presentation.configquestions.QuestionsConfigViewModel
import com.samuelsumbane.quizpalop.presentation.home.HomePageScreen
import com.samuelsumbane.quizpalop.presentation.maingamepage.MainPageScreen
import org.koin.compose.viewmodel.koinViewModel

class GameSessionScreen : Screen {
    @Composable
    override fun Content() {
        GameSessionPage()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameSessionPage() {
    val navigator = LocalNavigator.currentOrThrow
    val configQuestionsViewModel = koinViewModel<QuestionsConfigViewModel>()
    val configQuestionsUiState by configQuestionsViewModel.questionsConfigUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        configQuestionsViewModel.readSavedCountryAndCategory(loadCategory = true)
    }

    Scaffold {

        @Composable
        fun ShowContent() {
            PageLayout(
                modifier = Modifier
                    .padding(it)
            ) {
                IconButton(
                    onClick = { navigator.push(HomePageScreen()) },
                    modifier = Modifier.align(Alignment.TopStart)
                ) { BackIcon() }

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    PageTitleText("Sessão")
                    HomePageOptionColumn() {
                        if (configQuestionsUiState.lastCategoryWasSaved) {
                            HomeOption("Continuar última sessão") {
                                navigator.push(
                                    MainPageScreen(
                                        configQuestionsUiState.questionsCountry,
                                        configQuestionsUiState.questionsCategory
                                    )
                                )
                            }
                        }

                        HomeOption("Escolher País e Categoria") {
                            navigator.push(QuestionsConfigScreen(PagesName.MainPage))
                        }

                    }
                }
            }
        }

        when (configQuestionsUiState.pageUiState) {
            QuestionsConfigPageUiState.Loading -> LoadingScreen()
            QuestionsConfigPageUiState.ShowContent -> ShowContent()
        }
    }
}