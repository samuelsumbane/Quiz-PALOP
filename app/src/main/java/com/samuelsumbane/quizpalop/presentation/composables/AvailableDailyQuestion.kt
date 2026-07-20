package com.samuelsumbane.quizpalop.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.samuelsumbane.quizpalop.presentation.home.HomeUiState
import com.samuelsumbane.quizpalop.presentation.home.HomeViewModel
import com.samuelsumbane.quizpalop.ui.theme.HomeOptionColor

@Composable
fun AvailableDailyQuestion(
    homeViewModel: HomeViewModel,
    homeUiState: HomeUiState,
    modifier: Modifier
) {
   Column(
       modifier = modifier
           .padding(14.dp)
           .background(HomeOptionColor, RoundedCornerShape(14.dp))
           .padding(6.dp)
       ) {
       Text("Desafio de hoje")
       Text("Nivel: ")
       TwoButtonsRow(
           text = "",
           outlinedText = "Dispensar",
           outlinedClicked = {},
           filledButtonText = "Jogar",
           onFilledButtonClicked = {

           }
       )
   }
}