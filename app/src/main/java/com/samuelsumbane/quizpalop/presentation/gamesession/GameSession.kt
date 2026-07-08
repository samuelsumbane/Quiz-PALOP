package com.samuelsumbane.quizpalop.presentation.gamesession

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.samuelsumbane.quizpalop.domain.model.PagesName
import com.samuelsumbane.quizpalop.presentation.composables.HomeOption
import com.samuelsumbane.quizpalop.presentation.composables.HomePageOptionColumn
import com.samuelsumbane.quizpalop.presentation.composables.PageLayout
import com.samuelsumbane.quizpalop.presentation.composables.PageTitleText
import com.samuelsumbane.quizpalop.presentation.configquestions.QuestionsConfigScreen
import com.samuelsumbane.quizpalop.presentation.configquestions.QuestionsConfigViewModel
import org.koin.compose.viewmodel.koinViewModel

class GameSessionScreen : Screen {
    @Composable
    override fun Content() {
        GameSessionPage()
    }
}

@Composable
fun GameSessionPage() {
    val navigator = LocalNavigator.currentOrThrow
    val configQuestionsViewModel = koinViewModel<QuestionsConfigViewModel>()
    val configQuestionsUiState by configQuestionsViewModel.questionsConfigUiState.collectAsStateWithLifecycle()

    Scaffold {
        PageLayout(
            modifier = Modifier
                .padding(it)
        ) {
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
//                    navigator.push(PreQuestionsConfigScreen())
                        }
                    }

                    HomeOption("Escolher País e Categoria") {
                        navigator.push(QuestionsConfigScreen(PagesName.MainPage))
                    }

                }
            }
        }
    }
}