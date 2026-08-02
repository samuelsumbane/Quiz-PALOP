package com.samuelsumbane.quizpalop.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.samuelsumbane.quizpalop.presentation.composables.AppCheckBox
import com.samuelsumbane.quizpalop.presentation.composables.BackIcon
import com.samuelsumbane.quizpalop.presentation.composables.ConfigItem
import com.samuelsumbane.quizpalop.presentation.composables.PageLayout
import com.samuelsumbane.quizpalop.presentation.composables.PageTitleText
import com.samuelsumbane.quizpalop.presentation.composables.appBackground
import com.samuelsumbane.quizpalop.presentation.home.HomePageScreen
import org.koin.compose.viewmodel.koinViewModel

class SettingsScreen : Screen {
    @Composable
    override fun Content() {
        SettingsPage()
    }
}

@Composable
fun SettingsPage() {
    val settingViewModel = koinViewModel<SettingsPageViewModel>()
    val settingsUiState by settingViewModel.settingsUiState.collectAsStateWithLifecycle()

    val navigator = LocalNavigator.currentOrThrow

    Scaffold { paddingValues ->
        PageLayout(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            IconButton(
                onClick = { navigator.push(HomePageScreen())}
            ) { BackIcon() }

            PageTitleText(
                text = "Definições",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 60.dp)
            )

           LazyColumn(
              modifier = Modifier.align(Alignment.Center)
           ) {
               items(1) {
                   ConfigItem(
                       title = "Som",
                       description = "Emitir som ao clicar numa das opções",
                   ) {
                       AppCheckBox(
                           checked = settingsUiState.playSoundOnTap,
                       ) {
                           settingViewModel.onEvent(SettingsPageUiEvents.OnToggleSoundState)
                       }
                   }

                   ConfigItem(
                       title = "Vibração",
                       description = "Vibrar ao clicar numa das opções",
                   ) {
                       AppCheckBox(checked = settingsUiState.vibrateOnTap) {
                           settingViewModel.onEvent(SettingsPageUiEvents.OnToggleVibrateState)
                       }
                   }

                   ConfigItem(
                       title = "Notificações",
                       description = "Permitir notificações",
                   ) {
                       AppCheckBox(checked = settingsUiState.postNotifications) {
                           settingViewModel.onEvent(SettingsPageUiEvents.OnTogglePostNotifications)
                       }
                   }
               }
           }
       }
    }
}