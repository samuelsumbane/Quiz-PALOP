package com.samuelsumbane.quizpalop.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
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
        painter = painterResource(R.drawable.back),
        contentDescription = "Navigate up",
        modifier = Modifier.size(24.dp)
    )
}

@Composable
fun ForwardIcon() {
    Icon(
        painter = painterResource(R.drawable.forward),
        contentDescription = "Forward",
        modifier = Modifier.size(24.dp)
    )
}
@Composable
fun LockIcon() = NormalIconContainer(painterResource(R.drawable.lockicon), "locked")

@Composable
fun GameIcon() = IconContainer(painterResource(R.drawable.playgame), "play game",)

@Composable
fun DuelIcon() = IconContainer(painterResource(R.drawable.duel), "duel icon")

@Composable
fun ProgressIcon() = IconContainer(painterResource(R.drawable.progress), "progress icon")



@Composable
fun NormalIconContainer(painter: Painter, contentDescription: String) {
    Icon(painter = painter, contentDescription = contentDescription, modifier = Modifier.size(24.dp))
}
@Composable
fun IconContainer(painter: Painter, contentDescription: String) {
    Icon(painter = painter, contentDescription = contentDescription, modifier = Modifier.size(28.dp))
}