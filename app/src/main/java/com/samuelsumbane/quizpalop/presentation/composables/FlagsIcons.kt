package com.samuelsumbane.quizpalop.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.samuelsumbane.quizpalop.R
import com.samuelsumbane.quizpalop.presentation.aboutcountries.AboutCountriesScreen

@Composable
fun LoadFlag(
    painterId: Int,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Image(
        painterResource(painterId),
        contentDescription = contentDescription,
        modifier = modifier
            .padding(7.dp)
            .size(40.dp)
    )
}

@Composable
fun FlagsComponents() {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val navigator = LocalNavigator.currentOrThrow

        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.Center
        ) {
            LoadFlag(painterId = R.drawable.minangola, "Angoloa")
            LoadFlag(painterId = R.drawable.capeverde, "Cape Verde")
            LoadFlag(painterId = R.drawable.guineabissau, "Guine Bissau")
            LoadFlag(painterId = R.drawable.minmozambique, "Mozambique")
            LoadFlag(painterId = R.drawable.saotomeandprincipe, "Sao tome And Principe")
        }

        ButtonOutlined(text = "Conheça o país") { navigator.push(AboutCountriesScreen()) }
    } }