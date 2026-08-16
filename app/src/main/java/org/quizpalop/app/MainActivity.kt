package org.quizpalop.app

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.google.android.gms.ads.MobileAds
import org.koin.compose.viewmodel.koinViewModel
import org.quizpalop.app.core.notifications.NotificationIntent
import org.quizpalop.app.presentation.dailychallenge.DailyChallengeScreen
import org.quizpalop.app.presentation.dailychallenge.DailyChallengeViewModel
import org.quizpalop.app.presentation.home.HomePageScreen
import org.quizpalop.app.ui.theme.QuizPALOPTheme

class MainActivity : ComponentActivity() {
    private val openDailyChallenge = mutableStateOf(false)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        handleIntent(intent)

        MobileAds.initialize(this)

        enableEdgeToEdge()
        setContent {

            QuizPALOPTheme {
//                Navigator(MainPageScreen(Countries.Mz, Category.History))
//                Navigator(GameSessionScreen())
//                Navigator(QuestionsConfigScreen(PagesName.MainPage))
                Navigator(HomePageScreen()) { navigator ->
                    val shouldOpenDaily by openDailyChallenge
                    if (shouldOpenDaily) {
                        val dailyChallengeViewModel = koinViewModel<DailyChallengeViewModel>()
                        val state by dailyChallengeViewModel.dailychallengeUiState.collectAsStateWithLifecycle()

                        LaunchedEffect(Unit) { dailyChallengeViewModel.getAllSavedDailyQuestions() }
                        LaunchedEffect(state.dailyQuestionId) {
                            state.dailyQuestionId?.let { questionId ->
                                navigator.push(DailyChallengeScreen(questionId))
                                openDailyChallenge.value = false
                            }
                        }
                    }
                    CurrentScreen()
                }
//                Navigator(SettingsScreen())
//                Navigator(DailyChallengeScreen("mz_01"))
//                Navigator(AboutCountriesScreen())
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent?.getBooleanExtra(NotificationIntent.EXTRA_OPEN_DAILY_CHALLENGE, false) == true) {
            openDailyChallenge.value = true
        }
    }
}