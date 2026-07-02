package com.samuelsumbane.quizpalop.presentation.configquestions

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.protobuf.LazyStringArrayList.emptyList
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.samuelsumbane.quizpalop.domain.model.Category
import com.samuelsumbane.quizpalop.domain.model.PagesName
import com.samuelsumbane.quizpalop.domain.model.SoundEvent
import com.samuelsumbane.quizpalop.domain.repository.SoundManager
import com.samuelsumbane.quizpalop.presentation.composables.PageLayout
import com.samuelsumbane.quizpalop.presentation.composables.PageTitleText
import com.samuelsumbane.quizpalop.presentation.composables.RadioButtonGroup
import com.samuelsumbane.quizpalop.presentation.maingamepage.MainPageScreen
import com.samuelsumbane.quizpalop.presentation.userquestionspercentage.UserQuestionsPercentageViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.collections.emptyList


class QuestionsConfigScreen(val destination: PagesName) : Screen {
    @RequiresApi(Build.VERSION_CODES.S)
    @Composable
    override fun Content() {
        QuestionsConfigPage(destination)
    }
}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun QuestionsConfigPage(destination: PagesName) {
    val navigator = LocalNavigator.currentOrThrow
    val questionsConfigViewModel = koinViewModel<QuestionsConfigViewModel>()
    val questionsConfigUiState by questionsConfigViewModel.questionsConfigUiState.collectAsStateWithLifecycle()

    val userQuestionsPercentage = koinViewModel<UserQuestionsPercentageViewModel>()
    val userQuestionsPercentageUiState = userQuestionsPercentage.uiState.collectAsStateWithLifecycle()
    //
    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()
    val soundManager = remember { SoundManager(context) }
    val contentIndicator = if (questionsConfigUiState.questionConfig == QuestionConfig.SelectCountry) 1 else 2
    val progress by animateFloatAsState(
        targetValue = contentIndicator / 2.toFloat()
    )

    DisposableEffect(Unit) {
        onDispose {
            soundManager.release()
        }
    }

    LaunchedEffect(Unit) {
        questionsConfigViewModel.setQuestionConfig(QuestionConfig.SelectCategory)
//        progressViewModel.levelForLocked()
        questionsConfigViewModel.readSavedCategory()
        questionsConfigViewModel.readSavedLevel()
        questionsConfigViewModel.loadSavedQuestions()

//        startQuizGameViewModel.soundEvent.collect { event ->
//            if (event == SoundEvent.Click && startQuizGameUiState.soundState == SoundState.Playing) soundManager.playClick()
//        }
    }

    Scaffold { paddingValues ->

        PageLayout {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    PageTitleText(questionsConfigUiState.questionConfig.pageTitle)
                }

                Column(
                    modifier = Modifier
                        .padding(15.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("$contentIndicator de 2", fontWeight = FontWeight.SemiBold, color = Color.LightGray)
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        color = Color(0xFF179EFC),
                    )
                }

                Column(modifier = Modifier) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        RadioButtonGroup(
                            optionsList = questionsConfigUiState.questionConfig.configOptionsList,
                            lockedOptions = if (destination == PagesName.MainPage) {
                                when (questionsConfigUiState.questionConfig) {
                                    QuestionConfig.SelectCountry -> emptyList()
                                    QuestionConfig.SelectCategory-> questionsConfigUiState.lockLevelList
                                }
                            } else { emptyList() },
                            questionCategory = questionsConfigUiState.questionsCategory,
                            savedQuestionsId = if (destination == PagesName.MainPage) {
                                Triple(progressUiState.easyAnsweredQuestionsList, progressUiState.mediumAnsweredQuestionsList, progressUiState.hardAnsweredQuestionsList)
                            } else Triple(emptySet<Int>(), emptySet<Int>(), emptySet<Int>()),
                            selectedOption =
                                when (questionsConfigUiState.questionConfig) {
                                    QuestionConfig.SelectCategory -> Category.entries.first { it == startQuizGameUiState.questionsCategory }.value
                                    QuestionConfig.SelectLevel -> QuestionLevel.entries.first { it == startQuizGameUiState.questionsLevel }.value
                                }
                        ) { newValue ->
                            when (startQuizGameUiState.questionConfig) {
                                QuestionConfig.SelectCategory -> {
                                    startQuizGameViewModel.setGameCategory(QuestionCategory.entries.first { it.value == newValue })
                                }

                                QuestionConfig.SelectLevel -> startQuizGameViewModel.setGameLevel(
                                    QuestionLevel.entries.first { it.value == newValue }
                                )
                            }
                        }
                    }
                }

                BottomNavigation(
                    backButtonEnabled = startQuizGameUiState.questionConfig == QuestionConfig.SelectLevel,
                    onBackButtonClicked = {
                        if (startQuizGameUiState.questionConfig == QuestionConfig.SelectCategory) {
                            navigator.push(
                                if (destination == PagesName.MainPage) PreQuestionsConfigScreen() else HomeGameScreen()
                            )
                        } else {
                            startQuizGameViewModel.setQuestionConfig(QuestionConfig.SelectCategory)
                        }
                    },
                    onForwardButtonClicked = {
                        if (startQuizGameUiState.questionConfig == QuestionConfig.SelectCategory) {
                            startQuizGameViewModel.setQuestionConfig(QuestionConfig.SelectLevel)
                            coroutine.launch {
                                startQuizGameViewModel.saveSelectedCategory(startQuizGameUiState.questionsCategory.value)
                            }
                        } else {
                            navigator.push(
                                if (destination == PagesName.MainPage) {
                                    MainPageScreen(
                                        startQuizGameUiState.questionsCategory,
                                        startQuizGameUiState.questionsLevel
                                    )
                                } else {
//                                    DuelScreen(startQuizGameUiState.questionsCategory, startQuizGameUiState.questionsLevel)
                                }
                            )
                        }
                    }
                )
            }
        }
    }
}