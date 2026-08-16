package org.quizpalop.app.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.quizpalop.app.R
import org.quizpalop.app.presentation.aboutcountries.AboutCountriesScreen

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

        TextButton(
            onClick = { navigator.push(AboutCountriesScreen())},
            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onBackground)
        ) {
            Text(text = "Conheça o país", textDecoration = TextDecoration.Underline)
        }
    } }