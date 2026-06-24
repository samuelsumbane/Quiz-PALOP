package com.samuelsumbane.quizpalop.presentation.maingamepage.composables

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieConstants

@Composable
fun LoadAnimatedIcons(
    composition: LottieComposition?,
    modifier: Modifier = Modifier
) {
    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = modifier.size(180.dp)
    )
}