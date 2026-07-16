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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import com.samuelsumbane.quizpalop.domain.model.Countries
import com.samuelsumbane.quizpalop.domain.model.PagesName
import com.samuelsumbane.quizpalop.domain.model.PlayQuestionsNum
import com.samuelsumbane.quizpalop.domain.repository.SoundManager
import com.samuelsumbane.quizpalop.presentation.composables.BottomNavigation
import com.samuelsumbane.quizpalop.presentation.composables.LoadingScreen
import com.samuelsumbane.quizpalop.presentation.composables.PageLayout
import com.samuelsumbane.quizpalop.presentation.composables.PageTitleText
import com.samuelsumbane.quizpalop.presentation.composables.RadioButtonGroup
import com.samuelsumbane.quizpalop.presentation.duel.DuelScreen
import com.samuelsumbane.quizpalop.presentation.gamesession.GameSessionScreen
import com.samuelsumbane.quizpalop.presentation.homepage.HomePageScreen
import com.samuelsumbane.quizpalop.presentation.maingamepage.MainPageScreen
import com.samuelsumbane.quizpalop.presentation.userquestionspercentage.UserQuestionsPercentageViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


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
    val userQuestionsPercentageUiState by userQuestionsPercentage.uiState.collectAsStateWithLifecycle()
    //
    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()
    val soundManager = remember { SoundManager(context) }
    val forMainGame = if (destination == PagesName.MainPage) 2 else 3
    val contentIndicator = when (questionsConfigUiState.questionConfig) {
        QuestionConfig.SelectCountry -> 1
        QuestionConfig.SelectCategory -> 2
        else -> 3
    }
    val progress by animateFloatAsState(
        targetValue = contentIndicator / forMainGame.toFloat()
    )

    DisposableEffect(Unit) {
        onDispose {
            soundManager.release()
        }
    }

    LaunchedEffect(Unit) {
        questionsConfigViewModel.setQuestionConfig(QuestionConfig.SelectCountry)
        userQuestionsPercentage.levelForLocked()
        questionsConfigViewModel.readSavedCategory()
        questionsConfigViewModel.readSavedCountry()
        questionsConfigViewModel.loadSavedQuestions()

//        startQuizGameViewModel.soundEvent.collect { event ->
//            if (event == SoundEvent.Click && startQuizGameUiState.soundState == SoundState.Playing) soundManager.playClick()
//        }
    }

    Scaffold { paddingValues ->

        @Composable
        fun ShowPageContent() {
            PageLayout {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.SpaceAround
                ) {

                    Column(
                        modifier = Modifier
                            .padding(start = 15.dp, top = 0.dp, end = 15.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "$contentIndicator de $forMainGame",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .padding(20.dp)
                                .fillMaxWidth(),
                            color = Color(0xFF179EFC),
                            trackColor = MaterialTheme.colorScheme.background.copy(alpha = 0.9f)
                        )
                    }


                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        PageTitleText(questionsConfigUiState.questionConfig.pageTitle)
                    }

                    Column(modifier = Modifier) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            RadioButtonGroup(
                                allQuestions = questionsConfigUiState.questions,
                                optionsList = questionsConfigUiState.questionConfig.configOptionsList,
                                lockedOptions = when (questionsConfigUiState.questionConfig) {
                                        QuestionConfig.SelectCountry, QuestionConfig.SelectQuestionsLength -> emptyList()
                                        QuestionConfig.SelectCategory -> userQuestionsPercentageUiState.lockLevelList
                                    },
                                questionsCountry = questionsConfigUiState.questionsCountry,
                                savedQuestionsId = if (destination == PagesName.MainPage) {
                                    Triple(
                                        userQuestionsPercentageUiState.easyAnsweredQuestionsList,
                                        userQuestionsPercentageUiState.mediumAnsweredQuestionsList,
                                        userQuestionsPercentageUiState.hardAnsweredQuestionsList
                                    )
                                } else Triple(
                                    emptySet<String>(),
                                    emptySet<String>(),
                                    emptySet<String>()
                                ),
                                selectedOption =
                                    when (questionsConfigUiState.questionConfig) {
                                        QuestionConfig.SelectCategory -> Category.entries.first { it == questionsConfigUiState.questionsCategory }.categoryName
                                        QuestionConfig.SelectCountry -> Countries.entries.first { it == questionsConfigUiState.questionsCountry }.countryName
                                        QuestionConfig.SelectQuestionsLength -> PlayQuestionsNum.entries.first { it == questionsConfigUiState.questionsCount }.num
                                    }
                            ) { newValue ->
                                when (questionsConfigUiState.questionConfig) {
                                    QuestionConfig.SelectCountry -> questionsConfigViewModel.setGameCountry(
                                        newValue
                                    )

                                    QuestionConfig.SelectCategory -> questionsConfigViewModel.setGameCategory(
                                        newValue
                                    )

                                    QuestionConfig.SelectQuestionsLength -> questionsConfigViewModel.setGamePlayQuestionsLen(
                                        newValue
                                    )
                                }
                            }
                        }
                    }

                    BottomNavigation(
                        backButtonEnabled = contentIndicator == forMainGame,
                        onBackButtonClicked = {
                            if (questionsConfigUiState.questionConfig == QuestionConfig.SelectCountry) {
                                navigator.push(if (destination == PagesName.MainPage) GameSessionScreen() else HomePageScreen())
                            } else {
                                if (questionsConfigUiState.questionConfig == QuestionConfig.SelectCategory)
                                    questionsConfigViewModel.setQuestionConfig(QuestionConfig.SelectCountry)
                                else
                                    questionsConfigViewModel.setQuestionConfig(QuestionConfig.SelectCategory)
                            }
                        },
                        onForwardButtonClicked = {
                            if (questionsConfigUiState.questionConfig == QuestionConfig.SelectCountry) {
                                questionsConfigViewModel.setQuestionConfig(QuestionConfig.SelectCategory)
                                coroutine.launch {
                                    questionsConfigViewModel.saveSelectedCountry(
                                        questionsConfigUiState.questionsCountry.countryName
                                    )
                                }
                            } else {
                                if (destination == PagesName.MainPage) {
                                    navigator.push(
                                        MainPageScreen(
                                            questionsConfigUiState.questionsCountry,
                                            questionsConfigUiState.questionsCategory
                                        )
                                    )
                                } else {
                                    if (questionsConfigUiState.questionConfig == QuestionConfig.SelectCategory) {
                                        questionsConfigViewModel.setQuestionConfig(QuestionConfig.SelectQuestionsLength)
                                    } else {
                                        navigator.push(
                                            DuelScreen(
                                                questionsConfigUiState.questionsCountry,
                                                questionsConfigUiState.questionsCategory,
                                                questionsConfigUiState.questionsCount.num.toInt()
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }

        when (questionsConfigUiState.pageUiState) {
            QuestionsConfigPageUiState.Loading -> LoadingScreen()
            QuestionsConfigPageUiState.ShowContent -> ShowPageContent()
        }
    }
}