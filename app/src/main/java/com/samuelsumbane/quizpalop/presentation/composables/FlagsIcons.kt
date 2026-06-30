package com.samuelsumbane.quizpalop.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.samuelsumbane.quizpalop.R

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
            .size(50.dp)
    )
}

@Composable
fun FlagsComponents() {
    LazyRow(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        items(1) {
            LoadFlag(painterId = R.drawable.minangola, "Angoloa")
            LoadFlag(painterId = R.drawable.capeverde, "Cape Verde")
            LoadFlag(painterId = R.drawable.guineabissau, "Guine Bissau")
            LoadFlag(painterId = R.drawable.minmozambique, "Mozambique")
            LoadFlag(painterId = R.drawable.saotomeandprincipe, "Sao tome And Principe")
        }
    }
}