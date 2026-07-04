package com.samuelsumbane.quizpalop.presentation.gamesession

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.samuelsumbane.quizpalop.presentation.composables.HomeOption
import com.samuelsumbane.quizpalop.presentation.composables.HomePageOptionColumn
import com.samuelsumbane.quizpalop.presentation.composables.PageLayout
import com.samuelsumbane.quizpalop.presentation.composables.PageTitleText

class GameSessionScreen : Screen {
    @Composable
    override fun Content() {
        GameSessionPage()
    }
}

@Composable
fun GameSessionPage() {
    Scaffold {
        PageLayout(
            modifier = Modifier
                .padding(it)
        ) {
            PageTitleText("Sessão")


            HomePageOptionColumn() {
                HomeOption("Continuar última sessão") {
//                    navigator.push(PreQuestionsConfigScreen())
                }

                HomeOption("Escolher País e Categoria") {
                }

            }
        }
    }
}