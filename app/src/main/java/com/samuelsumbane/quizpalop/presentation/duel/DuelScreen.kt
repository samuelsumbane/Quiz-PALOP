package com.samuelsumbane.quizpalop.presentation.duel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.samuelsumbane.quizpalop.domain.model.Category
import com.samuelsumbane.quizpalop.domain.model.Countries
import com.samuelsumbane.quizpalop.domain.model.PagesName
import com.samuelsumbane.quizpalop.domain.model.SoundEvent
import com.samuelsumbane.quizpalop.domain.model.optionsLabels
import com.samuelsumbane.quizpalop.domain.repository.SoundManager
import com.samuelsumbane.quizpalop.presentation.composables.AppButton
import com.samuelsumbane.quizpalop.presentation.composables.ButtonOutlined
import com.samuelsumbane.quizpalop.presentation.composables.CenteredText
import com.samuelsumbane.quizpalop.presentation.composables.LoadingScreen
import com.samuelsumbane.quizpalop.presentation.composables.QuestionText
import com.samuelsumbane.quizpalop.presentation.configquestions.QuestionsConfigScreen
import com.samuelsumbane.quizpalop.presentation.configquestions.QuestionsConfigViewModel
import com.samuelsumbane.quizpalop.presentation.configquestions.SoundState
import com.samuelsumbane.quizpalop.presentation.homepage.HomePageScreen
import com.samuelsumbane.quizpalop.presentation.maingamepage.composables.LoadAnimatedIcons
import com.samuelsumbane.quizpalop.presentation.composables.OptionItem
import com.samuelsumbane.quizpalop.presentation.composables.TextQuestionColumn
import com.samuelsumbane.quizpalop.presentation.composables.appBackground
import com.samuelsumbane.quizpalop.ui.theme.HomeOptionColor
import org.koin.androidx.compose.koinViewModel

class DuelScreen(
    val country: Countries,
    val category: Category,
    val duelQuestionsSize: Int
) : Screen {
    @RequiresApi(Build.VERSION_CODES.S)
    @Composable
    override fun Content() {
        DuelPage(country, category, duelQuestionsSize)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun DuelPage(
    country: Countries,
    category: Category,
    duelQuestionsSize: Int
) {
    val duelViewModel = koinViewModel<DuelViewModel>()
    val duelUiState by duelViewModel.duelUiState.collectAsStateWithLifecycle()
    val startGameviewModel = koinViewModel<QuestionsConfigViewModel>()
    val startGameUiState by startGameviewModel.questionsConfigUiState.collectAsStateWithLifecycle()

    val navigator = LocalNavigator.currentOrThrow
    val context = LocalContext.current
    val soundManager = remember { SoundManager(context) }


    LaunchedEffect(Unit) {
        duelViewModel.loadData(country, category, duelQuestionsSize)
        duelViewModel.loadNextQuestionForBothPlayers()

        duelViewModel.soundEvent.collect { event ->
            if (startGameUiState.soundState == SoundState.Playing) {
                when (event) {
                    SoundEvent.Correct -> soundManager.playCorrect()
                    SoundEvent.Wrong -> soundManager.playWrong()
                    SoundEvent.Click -> soundManager.playClick()
                    SoundEvent.CoinEarned -> soundManager.playCoinsEarned()
                }
            }
        }
    }



    @Composable
    fun pageContent() {
        Scaffold {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                AdversarioContent(
                    duelViewModel,
                    duelUiState,
                    playerData = duelUiState.firstPlayer,
                    modifier = Modifier
                        .fillMaxHeight(0.5f)
                        .fillMaxWidth()
                        .rotate(180f),
                )

                HorizontalDivider(thickness = 5.dp, color = Color.LightGray)

                AdversarioContent(
                    duelViewModel,
                    duelUiState,
                    playerData = duelUiState.secondPlayer,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }
        }
    }

    when (duelUiState.pageState) {
        PageState.Loading -> LoadingScreen()
        PageState.ShowContent -> pageContent()
        PageState.DisplayMessage -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(HomeOptionColor)
            ) {
                val firstPlayerRightAnsweredQuestions = duelUiState.firstPlayer.rightAnsweredQuestions
                val secondPlayerRightAnsweredQuestions = duelUiState.secondPlayer.rightAnsweredQuestions

                    if (firstPlayerRightAnsweredQuestions > secondPlayerRightAnsweredQuestions) {
                        WinnerContainer(
                            navigator,
                            duelViewModel,
                            value = firstPlayerRightAnsweredQuestions - secondPlayerRightAnsweredQuestions, modifier = Modifier.asFirstPlayerModifier())
                        DuelHorizontalDivider()
                        LoserContainer(
                            value = firstPlayerRightAnsweredQuestions - secondPlayerRightAnsweredQuestions,
                        )
                    } else if (firstPlayerRightAnsweredQuestions < secondPlayerRightAnsweredQuestions) {
                        LoserContainer(
                            value = secondPlayerRightAnsweredQuestions - firstPlayerRightAnsweredQuestions,
                            modifier = Modifier.asFirstPlayerModifier()
                        )
                        DuelHorizontalDivider()
                        WinnerContainer(
                            navigator,
                            duelViewModel,
                            value = secondPlayerRightAnsweredQuestions - firstPlayerRightAnsweredQuestions)
                    } else {
                        NoWinnerNoLoser(modifier = Modifier.asFirstPlayerModifier())
                        DuelHorizontalDivider()
                        NoWinnerNoLoser { ContinueDuelButtons(navigator, duelViewModel) }
                    }
            }
        }
    }
}


@Composable
fun Modifier.asFirstPlayerModifier(): Modifier {
    return this
        .fillMaxHeight(0.5f)
        .rotate(180f)
}

@Composable
fun WinnerContainer(
    navigator: Navigator,
    duelViewModel: DuelViewModel,
    value: Int,
    modifier: Modifier = Modifier,
) {
    val winnerPlayerIcon by rememberLottieComposition(
        LottieCompositionSpec.Asset("lottie/winnerplayer.lottie")
    )

    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(1) {

            LoadAnimatedIcons(winnerPlayerIcon, modifier = Modifier.size(130.dp))

            DuelMessageText("Parabéns", "Você foi o vencedor desta partida. Ganhou por $value pontos.")
            ContinueDuelButtons(navigator, duelViewModel)
        }
    }
}

@Composable
fun ContinueDuelButtons(
    navigator: Navigator,
    duelViewModel: DuelViewModel
) {
    Row(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, bottom = 25.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AppButton(text = "Nova partida") { duelViewModel.onEvent(DuelUiEvents.OnLoadNewDuelWithSameCategoryAndLevel) }
        AppButton("Outras questões") { navigator.push(QuestionsConfigScreen(PagesName.DuelPage)) }
    }
}

@Composable
fun LoserContainer(
    value: Int,
    modifier: Modifier = Modifier
) {
    val navigator = LocalNavigator.currentOrThrow
    val loserPlayerIcon by rememberLottieComposition(
        LottieCompositionSpec.Asset("lottie/gamer_sad.lottie")
    )
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(1) {
            LoadAnimatedIcons(loserPlayerIcon)
            DuelMessageText("Não foi vencedor desta partida", "Perdeu por $value pontos.")
            ButtonOutlined("Fechar duelo", dangerMode = true) { navigator.push(HomePageScreen()) }
        }
    }
}

@Composable
fun NoWinnerNoLoser(
    modifier: Modifier = Modifier,
    content: (@Composable () -> Unit)? = null
) {
    Column(
        modifier = modifier
            .padding(top = 20.dp)
            .appBackground()
    ) {
        DuelMessageText("Empatado!","Não houve vencedor nesta partida")
        content?.invoke()
    }
}




@Composable
fun AdversarioContent(
    duelViewModel: DuelViewModel,
    duelUiState: DuelUiState,
    playerData: PlayerData,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .appBackground()
    ) {
        NumText(
            title = "Tempo",
            text = playerData.playerTimer.toString(),
            Color(0xFFF19633),
            modifier = Modifier.align(Alignment.TopStart)
        )

        NumText(
            title = "Acertos",
            text = playerData.rightAnsweredQuestions.toString(),
            Color(0xFF43C148),
            modifier = Modifier.align(Alignment.TopEnd)
        )

        playerData.question?.let { question ->
            LazyColumn(
                modifier = Modifier
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(1) {
                    TextQuestionColumn(
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth(0.85f)
                            .padding(10.dp)
                            .align(Alignment.TopCenter)
                    ) {
                        QuestionText(text = playerData.question.question ?: "", modifierFontSize = true)
                    }

//                    val currectQuestionOptions =
//                        if (playerData.name == PlayerName.FirstPlayer) duelUiState.firstPlayer.quest
//                        else duelUiState.secondPlayer.currentOptions

                    question.options.forEachIndexed { index, option ->
                        OptionItem(
                            prefixText = optionsLabels[index],
                            text = option,
                            backgroundColor = if (playerData.name == PlayerName.FirstPlayer) duelUiState.firstPlayer.optionsColors[index]
                            else duelUiState.secondPlayer.optionsColors[index]
                        ) { duelViewModel.onEvent(DuelUiEvents.OnCheckPlayerResponse(playerData, question.options[index]))}
                    }

                    Spacer(Modifier.padding(0.dp, 12.dp))
                }
            }
        }

    }
}

@Composable
fun NumText(
    title: String,
    text: String,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(5.dp)
            .background(Color.Transparent, RoundedCornerShape(50)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(title, fontSize = 9.sp, color = Color.White)
        Text(
            text = text,
            color = textColor,
            textAlign = TextAlign.Center,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DuelHorizontalDivider() {
    HorizontalDivider(thickness = 5.dp, color = Color.LightGray)
}

@Composable
fun DuelMessageText(
    title: String,
    text: String,
) {
    CenteredText(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(10.dp),
        color = Color.White
    )
    CenteredText(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(40.dp),
        color = Color.White
    )

}