package org.quizpalop.app.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.quizpalop.app.presentation.composables.AppCheckBox
import org.quizpalop.app.presentation.composables.BackIcon
import org.quizpalop.app.presentation.composables.ConfigItem
import org.quizpalop.app.presentation.composables.PageLayout
import org.quizpalop.app.presentation.composables.PageTitleText
import org.quizpalop.app.presentation.home.HomePageScreen
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
              modifier = Modifier
                  .fillMaxWidth(0.9f)
                  .background(MaterialTheme.colorScheme.background.copy(0.5f), RoundedCornerShape(16.dp))
                  .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
                  .align(Alignment.Center)
           ) {
               items(1) {
                   ConfigItem(
                       title = "Som",
                       description = "Emitir som quando estiver jogando",
                   ) {
                       AppCheckBox(
                           checked = settingsUiState.playSoundOnTap,
                       ) {
                           settingViewModel.onEvent(SettingsPageUiEvents.OnToggleSoundState)
                       }
                   }

                   AppHorizontalDivider()

                   ConfigItem(
                       title = "Vibração",
                       description = "Vibrar quando estiver jogando",
                   ) {
                       AppCheckBox(checked = settingsUiState.vibrateOnTap) {
                           settingViewModel.onEvent(SettingsPageUiEvents.OnToggleVibrateState)
                       }
                   }

                   AppHorizontalDivider()

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

@Composable
fun AppHorizontalDivider() {
    HorizontalDivider(
        modifier = Modifier.fillMaxWidth(),
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.background
    )
}