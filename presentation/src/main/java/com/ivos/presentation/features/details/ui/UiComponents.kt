package com.ivos.presentation.features.details.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ivos.common.utils.formatTempToString
import com.ivos.common.utils.formattedShortDayOfWeekDate
import com.ivos.domain.entities.Weather
import com.ivos.presentation.R

@Composable
fun InitialScreen(
    modifier: Modifier = Modifier
) {

}

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier
) {

}

@Composable
fun UpcomingWeatherBlock(
    modifier: Modifier = Modifier,
    weather: List<Weather>,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(R.string.upcoming_title),
                style = MaterialTheme.typography.bodyLarge,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceAround
            ) {
                weather.forEach {
                    UpcomingWeatherItemCard(weather = it)
                }
            }
        }
    }
}

@Composable
private fun UpcomingWeatherItemCard(
    modifier: Modifier = Modifier,
    weather: Weather,
) {
    Box(
        modifier = modifier
            .size(width = 100.dp, height = 100.dp)
            .background(
                color = MaterialTheme.colorScheme.onBackground,
                shape = MaterialTheme.shapes.medium
            )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = weather.tempC.formatTempToString(),
                color = MaterialTheme.colorScheme.background,
                style = MaterialTheme.typography.bodyMedium,
            )

            AsyncImage(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp),
                model = weather.iconUrl,
                contentDescription = ""
            )

            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = weather.date.formattedShortDayOfWeekDate(),
                color = MaterialTheme.colorScheme.background,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}
