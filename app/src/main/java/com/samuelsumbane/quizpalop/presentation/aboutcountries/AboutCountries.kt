package com.samuelsumbane.quizpalop.presentation.aboutcountries

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.samuelsumbane.quizpalop.data.repository.countriesData
import com.samuelsumbane.quizpalop.domain.model.CountryInfo
import com.samuelsumbane.quizpalop.presentation.composables.BackIcon
import com.samuelsumbane.quizpalop.presentation.composables.CountryInfoRows
import com.samuelsumbane.quizpalop.presentation.composables.LoadFlag
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
                .fillMaxSize()
                .appBackground()
        ) {

            val pagerState = rememberPagerState(initialPage = 0, pageCount = { countriesData.size })

            @Composable
            fun pageContent(countryInfo: CountryInfo) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.9f)
                        .background(MaterialTheme.colorScheme.background.copy(0.5f),
                            RoundedCornerShape(12.dp)
                        )
                ) {
                    with(countryInfo) {
                        Row(
                            modifier = Modifier
                                .padding(bottom = 40.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = country.countryName,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold
                            )
                            LoadFlag(
                                painterId = flagPath,
                                contentDescription = country.countryName,
                                Modifier.size(65.dp)
                            )
                        }

                        LazyColumn(
                            modifier = Modifier
                                .padding(15.dp)
                                .align(Alignment.Center)
                        ) {
                            items(1) {
                                CountryInfoRows(
                                    listOf(
                                        capital,
                                        corrency,
                                        independencia,
                                        area,
                                        paisesVisinhos,
                                        fusoHorario
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = { navigator.push(HomePageScreen()) },
                        modifier = Modifier
                    ) { BackIcon() }

                    Text("${pagerState.currentPage + 1}/5")
                }


                HorizontalPager(
                    state = pagerState, pageSpacing = 15.dp,
                    contentPadding = PaddingValues(horizontal = 38.dp),
                ) { page ->
                    pageContent(countryInfo = countriesData[page])
                }
            }
        }
    }
}