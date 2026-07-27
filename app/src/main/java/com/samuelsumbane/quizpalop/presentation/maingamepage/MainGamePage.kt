package com.samuelsumbane.quizpalop.presentation.maingamepage

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.samuelsumbane.quizpalop.R
import com.samuelsumbane.quizpalop.domain.model.Category
import com.samuelsumbane.quizpalop.domain.model.Countries
import com.samuelsumbane.quizpalop.domain.model.HelpOption
import com.samuelsumbane.quizpalop.domain.model.PagesName
import com.samuelsumbane.quizpalop.domain.model.SoundEvent
import com.samuelsumbane.quizpalop.domain.model.optionsLabels
import com.samuelsumbane.quizpalop.domain.repository.RewardedAdManager
import com.samuelsumbane.quizpalop.domain.repository.SoundManager
import com.samuelsumbane.quizpalop.presentation.composables.AppButton
import com.samuelsumbane.quizpalop.presentation.composables.GameBottomButton
import com.samuelsumbane.quizpalop.presentation.composables.GameTopStatusBar
import com.samuelsumbane.quizpalop.presentation.composables.IconData
import com.samuelsumbane.quizpalop.presentation.composables.LoadingScreen
import com.samuelsumbane.quizpalop.presentation.composables.MessageContainer
import com.samuelsumbane.quizpalop.presentation.composables.MessageTexts
import com.samuelsumbane.quizpalop.presentation.composables.MessageUi
import com.samuelsumbane.quizpalop.presentation.composables.OptionItem
import com.samuelsumbane.quizpalop.presentation.composables.QuestionText
import com.samuelsumbane.quizpalop.presentation.composables.TextQuestionColumn
import com.samuelsumbane.quizpalop.presentation.composables.TwoButtonsRow
import com.samuelsumbane.quizpalop.presentation.composables.appBackground
import com.samuelsumbane.quizpalop.presentation.configquestions.QuestionsConfigScreen
import com.samuelsumbane.quizpalop.presentation.composables.LoadAnimatedIcons
import com.samuelsumbane.quizpalop.presentation.progress.ProgressPageScreen
import org.koin.androidx.compose.koinViewModel


class MainPageScreen(val country: Countries, val category: Category) : Screen {
    @Composable
    override fun Content() {
        MainPage(country, category)
    }
}

@Composable
fun MainPage(country: Countries, category: Category) {
    val mainPageViewModel = koinViewModel<MainGameViewModel>()
    val mainPageUiState by mainPageViewModel.mainGameUiState.collectAsStateWithLifecycle()
    val navigator = LocalNavigator.currentOrThrow
    val context = LocalContext.current
    val soundManager = remember { SoundManager(context) }
    val manager = remember { RewardedAdManager(context) }


    val activity = context as Activity

    DisposableEffect(Unit) {
        onDispose {
            soundManager.release()
        }
    }

    LaunchedEffect(Unit) {

        mainPageViewModel.loadQuestions(country, category)

        mainPageViewModel.soundEvent.collect { event ->
            if (mainPageUiState.soundState == SoundState.Playing) {
                when (event) {
                    SoundEvent.Correct -> soundManager.playCorrect()
                    SoundEvent.Wrong -> soundManager.playWrong()
                    SoundEvent.Click -> soundManager.playClick()
                    SoundEvent.CoinEarned -> soundManager.playCoinsEarned()
                }
            }
        }
    }


//    LaunchedEffect(mainPageUiState.timerState) {
//        if (mainPageUiState.gameTextMessage is GameTextMessage.Empty) {
//            mainPageViewModel.timerCounterExec()
//        }
//    }


    @Composable
    fun SelectedDataAlreadyAnswered() {

        val finishedLevelIcon by rememberLottieComposition(
            LottieCompositionSpec.Asset("lottie/allQAnswered.lottie")
        )

        val allQuestionsFineshedIcon by rememberLottieComposition(
            LottieCompositionSpec.Asset("lottie/trophy1.lottie")
        )


        Scaffold {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .appBackground()
            ) {
                Column(
                    modifier = Modifier
                        .background(color = Color.Transparent)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val navigator = LocalNavigator.currentOrThrow
                    MessageContainer {
                        when (val message = mainPageUiState.gameTextMessage) {
                            is GameTextMessage.AllQuestionsAnswered -> {
                                LoadAnimatedIcons(allQuestionsFineshedIcon)
                                MessageTexts(title = message.title, text = message.message)
                                AppButton("Ver progresso") { navigator.push(ProgressPageScreen()) }
                            }

                            is GameTextMessage.SelectedQuestionsAnswered -> {
                                LoadAnimatedIcons(finishedLevelIcon)
                                MessageTexts(title = "", message.message)
                                TwoButtonsRow(
//                                mainPageViewModel,
                                    text = message.confirmationText,
                                    outlinedText = "Prox. País/Categoria",
                                    outlinedClicked = {
                                        mainPageViewModel.tryToLoadNextCategoryInThisCategory()
                                    },
                                    filledButtonText = "Sel. Questões",
                                    onFilledButtonClicked = {
                                        mainPageViewModel.onSelectNewQuestionsGroup()
                                        navigator.push(QuestionsConfigScreen(PagesName.MainPage))
                                    }
                                )
                            }

                            else -> {}
                        }
                    }

                }
            }
        }
    }

    @Composable
    fun pageContent() {
        mainPageUiState.actualQuestion?.let { questionData ->

            Scaffold(
                bottomBar = {
                    AnimatedVisibility(
                        visible = mainPageUiState.gameTextMessage is GameTextMessage.Empty && mainPageUiState.lives > 0,
                        enter = slideInHorizontally(
                            initialOffsetX = { -it }
                        ) + fadeIn(),
                        exit = slideOutHorizontally(
                            targetOffsetX = { it }
                        ) + fadeOut()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .navigationBarsPadding()
                                .background(brush = Brush.linearGradient(
                                    colors = listOf(MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                                        MaterialTheme.colorScheme.background.copy(alpha = 0.5f))
                                ))
                        ) {
                            Row(
                                modifier = Modifier
//                                    .background(Color.Red)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                GameBottomButton(
                                    icon = IconData(R.drawable.fift_fift_icon, "fift fift"),
                                    buttonText = "50 50",
                                    requiredCoins = 15,
                                ) {
                                    mainPageViewModel.onEvent(MainGameUiEvents.OnHelp(HelpOption.FiftFift))
                                }

                                GameBottomButton(
                                    icon = IconData(R.drawable.outline_check_24, "correct answer"),
                                    buttonText = "R. correcta",
                                    requiredCoins = 25,
                                ) {
                                    mainPageViewModel.onEvent(MainGameUiEvents.OnHelp(HelpOption.RightOption))
                                }


                                Row(
                                    modifier = Modifier.width(45.dp),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    GameBottomButton(
                                        icon = IconData(R.drawable.door_open_fill, "Exit"),
                                        requiredCoins = 0,
                                        buttonText = "Sair",
                                    ) {
                                        mainPageViewModel.onEvent(MainGameUiEvents.OnExit)
                                    }
                                }
                            }
                        }
                    }
                },
            ) { padding ->
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .appBackground()
                        .padding(10.dp),
                ) {
                    AnimatedVisibility(mainPageUiState.lives < 1) {
                        MaxSizeBox {
                            NoMoreLivesUI(
                                navigator,
                                mainPageViewModel,
                                mainPageUiState,
                                manager,
                                activity,
                            )
                        }
                    }

                    AnimatedVisibility(mainPageUiState.lives >= 1) {
                        Box(modifier = Modifier) {
                            MaxSizeBox {
                                MessageUi(
                                    navigator,
                                    mainPageViewModel,
                                    mainPageUiState,
                                    activity,
                                    manager,
                                )
                            }

                            if (mainPageUiState.gameTextMessage is GameTextMessage.Empty) {
                                GameTopStatusBar(
                                    mainPageViewModel,
                                    mainPageUiState,
                                    modifier = Modifier
                                        .align(Alignment.TopCenter)
                                        .zIndex(5f)
                                )

                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.SpaceEvenly
                                ) {

                                    TextQuestionColumn(
                                        modifier = Modifier.padding(10.dp)
                                    ) {
                                        mainPageUiState.actualQuestion?.let { question ->
                                            QuestionText(
                                                question.question,
                                                modifierFontSize = false
                                            )
                                        }
                                    }

                                    mainPageUiState.actualQuestion?.let { question ->
                                        LazyColumn(
                                        ) {
                                            items(1) {
                                                question.options.forEachIndexed { index, option ->
                                                    OptionItem(
                                                        prefixText = optionsLabels[index],
                                                        text = option,
                                                        backgroundColor = mainPageUiState.optionsColors[index]
                                                    ) {
                                                        mainPageViewModel.onEvent(
                                                            MainGameUiEvents.OnCheckResponse(option)
                                                        )
                                                    }
                                                }
                                        Text(text = mainPageUiState.actualQuestionRightAnswer, color = MaterialTheme.colorScheme.onBackground)
                                            }
                                        }
                                    }
                                }
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
        MainPageState.QuestionsAnswered -> SelectedDataAlreadyAnswered()
    }

}

@Composable
fun MaxSizeBox(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) { content() }
}