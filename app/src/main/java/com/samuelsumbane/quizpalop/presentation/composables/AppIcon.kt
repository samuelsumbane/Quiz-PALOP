package com.samuelsumbane.quizpalop.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun DuelIcon() = NormalIconContainer(painterResource(R.drawable.person_versus), "duel icon")

@Composable
fun DailyChallengeIcon() = NormalIconContainer(painterResource(R.drawable.dailychallenge), "dailyChallenge icon")

@Composable
fun ProgressIcon() = IconContainer(painterResource(R.drawable.progress), "progress icon")

@Composable
fun HomeIcon(tint: Color = MaterialTheme.colorScheme.onBackground) = IconContainer(painterResource(R.drawable.homeicon), "home page", tint = tint)

@Composable
fun PrintScreenIcon(tint: Color = MaterialTheme.colorScheme.onBackground) =
    IconContainer(painterResource(R.drawable.screenshotframe), "pirnt screen icon", tint = tint)


@Composable
fun NormalIconContainer(painter: Painter, contentDescription: String) {
    Icon(painter = painter, contentDescription = contentDescription, modifier = Modifier.size(28.dp))
}

@Composable
fun IconContainer(painter: Painter, contentDescription: String, tint: Color = MaterialTheme.colorScheme.onBackground) {
    Icon(painter = painter, contentDescription = contentDescription, tint = tint, modifier = Modifier.size(28.dp))
}

@Composable
fun CheckIcon() {
    Icon(painterResource(
        R.drawable.check),
        "", tint = Color(0xFF09740E),
        modifier = Modifier.size(26.dp)
    )
}

@Composable
fun IconAndTextColumn(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    icon: @Composable () -> Unit
) {
    if (enabled) {
        Column(
            modifier = Modifier
                .clickable { onClick() },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            icon()
            Text(text = text, color = MaterialTheme.colorScheme.onBackground, fontSize = 12.sp)
        }
    }
}

@Composable
fun VibrateIcon() = NormalIconContainer(painterResource(R.drawable.vibrateicon), contentDescription = "Vibration active")

@Composable
fun VibrationOff() = NormalIconContainer(painterResource(R.drawable.vibrationoff), contentDescription = "Vibration off")
