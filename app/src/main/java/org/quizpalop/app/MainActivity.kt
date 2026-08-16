package org.quizpalop.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import cafe.adriel.voyager.navigator.Navigator
import com.google.android.gms.ads.MobileAds
import org.quizpalop.app.presentation.home.HomePageScreen
import org.quizpalop.app.ui.theme.QuizPALOPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        MobileAds.initialize(this)

        enableEdgeToEdge()
        setContent {

            QuizPALOPTheme {
//                Navigator(MainPageScreen(Countries.Mz, Category.History))
//                Navigator(GameSessionScreen())
//                Navigator(QuestionsConfigScreen(PagesName.MainPage))
                Navigator(HomePageScreen())
//                Navigator(SettingsScreen())
//                Navigator(DailyChallengeScreen("mz_01"))
//                Navigator(AboutCountriesScreen())
            }
        }
    }
}