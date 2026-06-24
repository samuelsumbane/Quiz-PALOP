package com.samuelsumbane.quizpalop.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LoadingScreen() {
    Column(
        Modifier
            .background(Color(0xFF676868))
            .fillMaxSize(),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(54.dp),
            color = Color(0xFF03A9F4),
            trackColor = Color.DarkGray,
        )
        Spacer(Modifier.height(20.dp))
        Text("Carregando...")
    }
}

//@Composable
//fun HomePageOptionColumn(
//    modifier: Modifier = Modifier,
//    content: @Composable () -> Unit
//) {
//    Column(
//        modifier = modifier
//            .padding(top = 30.dp)
//            .background(Color(0xBC9C9C9B), RoundedCornerShape(16.dp))
//            .padding(0.dp, 5.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) { content() }
//}
