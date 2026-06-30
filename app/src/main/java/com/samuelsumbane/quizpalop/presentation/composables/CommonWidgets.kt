package com.samuelsumbane.quizpalop.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.samuelsumbane.quizpalop.R
import com.samuelsumbane.quizpalop.ui.theme.HomeOptionColor

@Composable
fun HorizontalDividerWithText(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("$text ", color = Color.Black, fontWeight = FontWeight.SemiBold)
        HorizontalDivider(Modifier.fillMaxWidth())
    }
}

@Composable
fun BigExitAppIcon() {
    Icon(
        painter = painterResource(R.drawable.door_open_fill), "",
        tint = Color(0xFFC61C1C),
        modifier = Modifier.size(80.dp, 100.dp)
    )
}

@Composable
fun TwoButtonsRow(
    text: String,
    outlinedText: String,
    outlinedClicked: () -> Unit,
    filledButtonText: String,
    dangerMode: Boolean = false,
    onClick: () -> Unit
) {
    Column(modifier = Modifier
        .padding(start = 8.dp, top = 55.dp, end = 8.dp)
        .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) { Text(text, fontSize = 15.sp, textAlign = TextAlign.Center, color = Color.Black, fontWeight = FontWeight.SemiBold) }

        Row(
            modifier = Modifier
                .padding(0.dp, 25.dp)
                .fillMaxWidth()
                .padding(7.dp, 0.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ButtonOutlined(
                text = outlinedText,
                dangerMode = dangerMode,
                onClick = outlinedClicked,
            )

            AppButton(
                text = filledButtonText,
                dangerMode = dangerMode
            ) { onClick() }
        }
    }
}

@Composable
fun HomePageOptionColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .padding(top = 30.dp)
            .padding(0.dp, 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) { content() }
}

@Composable
fun HomeOption(text: String, onClick: () -> Unit) {
    Item(
        text = text,
        modifier = Modifier
            .padding(15.dp, 12.dp)
            .background(Color.Transparent, RoundedCornerShape(16.dp))
            .border(1.dp, HomeOptionColor, RoundedCornerShape(16.dp))
            .padding(10.dp),
        onClick = onClick
    )
}

@Composable
fun Item(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .height(28.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text, color = Color.White, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun PageTitleText(text: String) {
    Text(
        text = text,
        fontSize = 30.sp,
        fontWeight = FontWeight.ExtraBold,
        color = MaterialTheme.colorScheme.onPrimary
    )
}