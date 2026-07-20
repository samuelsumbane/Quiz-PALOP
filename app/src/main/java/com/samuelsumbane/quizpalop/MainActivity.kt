package com.samuelsumbane.quizpalop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cafe.adriel.voyager.navigator.Navigator
import com.google.android.gms.ads.MobileAds
import com.samuelsumbane.quizpalop.presentation.home.HomePageScreen
import com.samuelsumbane.quizpalop.ui.theme.QuizPALOPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MobileAds.initialize(this)

        enableEdgeToEdge()
        setContent {
            QuizPALOPTheme {
//                Navigator(MainPageScreen(Countries.Mz, Category.History))
//                Navigator(GameSessionScreen())
                Navigator(HomePageScreen())
//                Navigator(DuelScreen(Countries.Mz, Category.History, 2))
            }
        }
    }
}