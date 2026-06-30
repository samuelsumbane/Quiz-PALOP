//package com.samuelsumbane.quizpalop.presentation.progress
//
//import android.widget.ProgressBar
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import cafe.adriel.voyager.core.screen.Screen
//import cafe.adriel.voyager.navigator.LocalNavigator
//import cafe.adriel.voyager.navigator.currentOrThrow
//import com.samuelsumbane.quizpalop.domain.model.ProgressContentState
//import com.samuelsumbane.quizpalop.presentation.composables.LoadingScreen
//import com.samuelsumbane.quizpalop.presentation.composables.NavigateUpButton
//import com.samuelsumbane.quizpalop.presentation.composables.PageLayout
//import com.samuelsumbane.quizpalop.presentation.composables.PageTitleText
//import com.samuelsumbane.quizpalop.presentation.composables.ProgressBar
//import com.samuelsumbane.quizpalop.presentation.composables.TwoButtonsRow
//import org.koin.compose.viewmodel.koinViewModel
//
//class ProgressPageScreen : Screen {
//    @Composable
//    override fun Content() {
//        ProgressPage()
//    }
//}
//
//@Composable
//fun ProgressPage() {
//    val navigator = LocalNavigator.currentOrThrow
//
//    val progressViewModel: ProgressViewModel = koinViewModel()
//    val progressUiState by progressViewModel.progressUiState.collectAsState()
//
//    LaunchedEffect(Unit) {
//        progressViewModel.loadSavedQuestions(false)
//    }
//
//    PageLayout {
//        when (val messageData = progressUiState.progressContentState) {
//            ProgressContentState.ShowContent -> {
//                Column(
//                    modifier = Modifier
//                        .background(Color.Black.copy(alpha = 0.35f))
//                        .fillMaxSize(),
//                ) {
//                    Row(
//                        modifier = Modifier.padding(top = 25.dp, bottom = 10.dp)
//                    ) {
//                        NavigateUpButton { navigator.pop() }
//                    }
//
//                    Row(
//                        modifier = Modifier
//                            .padding(0.dp , 15.dp)
//                            .fillMaxWidth(),
//                        horizontalArrangement = Arrangement.Center
//                    ) {
//                        PageTitleText("Progresso")
//                    }
//
//                    Column(
//                        modifier = Modifier
//                            .padding(top = 10.dp)
//                            .weight(1f),
//                        verticalArrangement = Arrangement.Center
//                    ) {
//
//                        LazyColumn {
//                            items(1) {
//                                Column(
//                                    modifier = Modifier,
//                                    verticalArrangement = Arrangement.SpaceEvenly
//                                ) {
//                                    val allProgressPercentage =
//                                        (progressUiState.easyAnsweredQuestionsList + progressUiState.mediumAnsweredQuestionsList + progressUiState.hardAnsweredQuestionsList).size.toFloat() / progressUiState.allQuestionsList.size
//
//                                    ProgressBar(
//                                        actualPercentage = allProgressPercentage,
//                                        level = "Todo"
//                                    )
//
//                                    Column(
//                                        modifier = Modifier.padding(top = 30.dp)
//                                    ) {
//                                        ProgressBar(
//                                            actualPercentage = progressUiState.easyAnsweredQuestionsPercent,
//                                            level = "Simples"
//                                        )
//                                        ProgressBar(
//                                            actualPercentage = progressUiState.mediumAnsweredQuestionsPercent,
//                                            level = "Médio"
//                                        )
//                                        ProgressBar(
//                                            actualPercentage = progressUiState.hardAnsweredQuestionsPercent,
//                                            level = "Difícil"
//                                        )
//                                    }
//                                }
//
//                                Row(
//                                    modifier = Modifier
//                                        .padding(top = 55.dp, bottom = 25.dp)
//                                        .fillMaxWidth(),
//                                    horizontalArrangement = Arrangement.Center
//                                ) {
//                                    Button(
//                                        onClick = {
////                                            progressViewModel.changeProgressContentStateTo(
////                                                ProgressContentState.ConfirmExit(
////                                                    title = "Reiniciar progresso?",
////                                                    message = "Todo o progresso, pontuação e desbloqueios serão apagados."
////                                                )
////                                            )
//                                        },
//                                        colors = ButtonDefaults
//                                            .buttonColors(
//                                                containerColor = Color(0xFFE23C3C),
//                                                contentColor = Color.White
//                                            )
//                                    ) {
//                                        Text("Reiniciar o progresso")
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//
//            is ProgressContentState.ConfirmExit -> {
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize(),
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .padding(15.dp)
//                            .background(Color(0xBC9C9C9B), RoundedCornerShape(12.dp))
//                            .padding(12.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.Center
//                    ) {
//                        Text(
//                            text = messageData.title,
//                            fontWeight = FontWeight.SemiBold,
//                            fontSize = 20.sp,
//                            color = Color.Black,
//                            modifier = Modifier.padding(top = 18.dp)
//                        )
//
//                        TwoButtonsRow(
//                            text = messageData.message,
//                            outlinedText = "Cancelar",
//                            outlinedClicked = {
//                                progressViewModel.changeProgressContentStateTo(ProgressContentState.ShowContent)
//                            },
//                            filledButtonText = "Reininciar",
//                            dangerMode = true,
//                            onClick = { progressViewModel.resetProgress() }
//                        )
//                    }
//                }
//            }
//
//            ProgressContentState.Loading -> LoadingScreen()
//        }
//    }
//}