package org.quizpalop.app.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


data class IconData(val id: Int, val contentDescription: String)


@Composable
fun GameBottomButton(
    icon: IconData,
    buttonText: String,
    requiredCoins: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clickable {
                if (enabled) {
                    onClick()
                }
            }
            .padding(10.dp, 8.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (requiredCoins > 0) {
            ActionCostInCoins(
                requiredCoins,
                modifier = Modifier
                    .padding(start = 30.dp)
                    .offset(y = -5.dp)
                    .align(Alignment.TopEnd)
            )
        }

        Column {
            Icon(
                painter = painterResource(icon.id),
                icon.contentDescription,
                tint = if (!enabled) Color.Gray else MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(25.dp)
            )
            Spacer(Modifier.height(5.dp))
            Text(
                buttonText,
                style = MaterialTheme.typography.bodySmall,
                color = if (!enabled) Color.Gray else MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}


@Composable
fun ActionCostInCoins(coinsNum: Int, modifier: Modifier) {
    verticallyCenteredRowContent(modifier = modifier) {
        Row(
            modifier = Modifier
                .background(Color(0xFF10151D), RoundedCornerShape(5.dp))
                .padding(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CoinIcon(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(12.dp),
                withPadding = false
            )
            Text(
                coinsNum.toString(),
                style = MaterialTheme.typography.bodySmall,
                color = Color.LightGray
            )
        }
    }
}
