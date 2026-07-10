package com.samuelsumbane.quizpalop.presentation.homepage

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.samuelsumbane.quizpalop.presentation.composables.FlagsComponents
import com.samuelsumbane.quizpalop.presentation.composables.HomeOption
import com.samuelsumbane.quizpalop.presentation.composables.HomePageOptionColumn
import com.samuelsumbane.quizpalop.presentation.composables.appBackground
import com.samuelsumbane.quizpalop.presentation.gamesession.GameSessionPage
import com.samuelsumbane.quizpalop.presentation.gamesession.GameSessionScreen
import com.samuelsumbane.quizpalop.presentation.maingamepage.composables.OptionItem
import com.samuelsumbane.quizpalop.presentation.progress.ProgressPageScreen

class HomePageScreen : Screen {
    @Composable
    override fun Content() {
        HomePage()
    }
}

@Composable
fun HomePage() {
    val navigator = LocalNavigator.currentOrThrow

    Scaffold {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .appBackground()
        ) {

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
                    HomeOption("Jogar") { navigator.push(GameSessionScreen()) }
                    HomeOption("Dois jogadores") { navigator.push(GameSessionScreen())}
                    HomeOption("Progresso") { navigator.push(ProgressPageScreen()) }


                }

            }


            Text(
                text = "Versão: 1.0.0",
                color = Color.Gray,
                fontSize = 10.sp,
                modifier = Modifier
                    .padding(10.dp)
                    .background(Color.Black.copy(0.1f), RoundedCornerShape(7.dp))
                    .padding(5.dp)
                    .align(Alignment.BottomEnd)
                )
        }
    }
}