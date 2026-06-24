package com.samuelsumbane.quizpalop.presentation.maingamepage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import com.samuelsumbane.quizpalop.presentation.composables.LoadingScreen
import com.samuelsumbane.quizpalop.presentation.maingamepage.composables.OptionItem
import com.samuelsumbane.quizpalop.presentation.maingamepage.composables.QuestionText
import com.samuelsumbane.quizpalop.presentation.maingamepage.composables.TextQuestionColumn
import org.koin.androidx.compose.koinViewModel

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
        Scaffold { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextQuestionColumn(
                    modifier = Modifier
                ) {
                    mainPageUiState.actualQuestion?.let { question ->
                        QuestionText(question.question, modifierFontSize = false)
                    }
                }

                mainPageUiState.actualQuestion?.let { question ->
                    val questionOptions = question.options.shuffled()

                    LazyColumn {
                        items(1) {
                            questionOptions.forEachIndexed { index, options ->
                                OptionItem(
                                    text = questionOptions[index],
                                    background = mainPageUiState.optionsColors[index]
                                ) { }
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