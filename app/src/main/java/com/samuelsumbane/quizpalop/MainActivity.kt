package com.samuelsumbane.quizpalop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.Navigator
import com.google.android.gms.ads.MobileAds
import com.samuelsumbane.quizpalop.domain.model.Category
import com.samuelsumbane.quizpalop.domain.model.Countries
import com.samuelsumbane.quizpalop.presentation.gamesession.GameSessionScreen
import com.samuelsumbane.quizpalop.presentation.homepage.HomePageScreen
import com.samuelsumbane.quizpalop.presentation.maingamepage.MainPageScreen
import com.samuelsumbane.quizpalop.presentation.progress.ProgressPageScreen
import com.samuelsumbane.quizpalop.ui.theme.QuizPALOPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MobileAds.initialize(this)

        enableEdgeToEdge()
        setContent {
            QuizPALOPTheme {
                Navigator(MainPageScreen(countryId = Countries.Mz.code, Category.History.categoryMeaning))
//                Navigator(GameSessionScreen())
//                Navigator(HomePageScreen())
            }
        }
    }
}