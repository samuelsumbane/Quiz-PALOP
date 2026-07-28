package com.samuelsumbane.quizpalop.presentation.aboutcountries

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Down
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Up
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.samuelsumbane.quizpalop.domain.model.Countries
import com.samuelsumbane.quizpalop.domain.model.CountryInfo
import com.samuelsumbane.quizpalop.domain.model.getCountryInfo
import com.samuelsumbane.quizpalop.presentation.composables.AppButton
import com.samuelsumbane.quizpalop.presentation.composables.BackIcon
import com.samuelsumbane.quizpalop.presentation.composables.ButtonOutlined
import com.samuelsumbane.quizpalop.presentation.composables.KeyValueRowText
import com.samuelsumbane.quizpalop.presentation.composables.appBackground
import com.samuelsumbane.quizpalop.presentation.home.HomePageScreen

class AboutCountriesScreen : Screen {
    @Composable
    override fun Content() {
        AboutCountries()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutCountries() {
    val navigator = LocalNavigator.currentOrThrow
    val horizontalScroll = rememberScrollState(40)

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .appBackground()
        ) {
            var selectedTabIndex by remember { mutableIntStateOf(0) }
            val tabs = Countries.entries.map { it.countryName }

            @Composable
            fun tabContent(country: CountryInfo) {
                Row(
                ) {

                }

                LazyColumn(
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    items(1) {
                        KeyValueRowText("Capital", country.capital)
                        KeyValueRowText("Moeda", country.corrency)
                        KeyValueRowText("Independência", country.independencia)
                        KeyValueRowText("Aréa", country.area)
                        KeyValueRowText("Países vizinhos", country.paisesVisinhos)
                        KeyValueRowText("Fuso horário", country.fusoHorario)
                    }
                }
            }

            Column {
                IconButton(
                    onClick = { navigator.push(HomePageScreen()) },
                    modifier = Modifier
                        .padding(6.dp)
                ) { BackIcon() }

                Row(
                    modifier = Modifier
                        .padding(10.dp, 0.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BackIcon()
                     Row(
                         modifier = Modifier
                             .horizontalScroll(horizontalScroll)
                     ) {
                         tabs.forEachIndexed { index, tab ->
                             AppButton(
                                 text = tab,
                                 modifier = Modifier
                                     .padding(8.dp)
                             ) { selectedTabIndex = index }
                         }
                    }
                    BackIcon()
                }

                AnimatedContent(
                    targetState = selectedTabIndex,
                    transitionSpec = {
                        slideIntoContainer(
                            animationSpec = tween(400, easing = EaseIn), towards = Up
                        ).togetherWith(
                            slideOutOfContainer(
                                animationSpec = tween(450, easing = EaseOut), towards = Down
                            )
                        )
                    },
                ) { selectedTabIndex ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        Column(
                            modifier = Modifier.align(Alignment.Center)
                        ) {
                            when (selectedTabIndex) {
                                0 -> tabContent(getCountryInfo(Countries.Angola))
                                1 -> tabContent(getCountryInfo(Countries.Cv))
                                2 -> tabContent(getCountryInfo(Countries.Gw))
                                3 -> tabContent(getCountryInfo(Countries.Mz))
                                4 -> tabContent(getCountryInfo(Countries.Stp))
                            }
                        }
                    }
                }
            }
        }

    }
}