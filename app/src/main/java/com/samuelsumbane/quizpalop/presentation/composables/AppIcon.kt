package com.samuelsumbane.quizpalop.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.samuelsumbane.quizpalop.R

@Composable
fun CoinIcon(
    modifier: Modifier = Modifier,
    withPadding: Boolean = true
) {
    Image(
        painter = painterResource(R.drawable.coin_2),
        contentDescription = "coin icon",
        modifier = if (withPadding)
            modifier.padding(10.dp) else modifier
    )
}

@Composable
fun BackIcon() {
    Icon(
        painter = painterResource(R.drawable.outline_check_24),
        contentDescription = "Navigate up",
    )
}

@Composable
fun LockIcon() = Icon(painterResource(R.drawable.volume_up_fill), "locked")

