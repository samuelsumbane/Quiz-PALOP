package com.samuelsumbane.quizpalop

import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.navigator.Navigator
import com.google.android.gms.ads.MobileAds
import com.samuelsumbane.quizpalop.presentation.dailychallenge.DailyChallengeScreen
import com.samuelsumbane.quizpalop.presentation.home.HomePageScreen
import com.samuelsumbane.quizpalop.ui.theme.QuizPALOPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val deveExecutar = intent.getBooleanExtra("executar_funcao", false)
        MobileAds.initialize(this)

        enableEdgeToEdge()
        setContent {
            LaunchedEffect(Unit) {
                if (deveExecutar) {

                }
            }

            QuizPALOPTheme {
//                Navigator(MainPageScreen(Countries.Mz, Category.History))
//                Navigator(GameSessionScreen())
                Navigator(HomePageScreen())
//                Navigator(DuelScreen(Countries.Mz, Category.History, 2))
//                Navigator(DailyChallengeScreen("mz_01"))
            }
        }
    }

    // Certo
    override fun onNewIntent(intent: Intent, caller: ComponentCaller) {
        super.onNewIntent(intent, caller)
        setIntent(intent)
        val deveExecutar = intent.getBooleanExtra("executar_funcao", false)
        if (deveExecutar) {
            // ...
        }
    }
}