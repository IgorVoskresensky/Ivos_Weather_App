package com.ivos.presentation.features.details.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ivos.common.utils.formatTempToString
import com.ivos.common.utils.formattedFullDate
import com.ivos.domain.entities.Forecast

@Composable
fun DetailsContentScreen(
    modifier: Modifier = Modifier,
    forecast: Forecast,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Text(
            modifier = Modifier.padding(bottom = 16.dp),
            text = forecast.currentWeather.description,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = forecast.currentWeather.tempC.formatTempToString(),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 70.sp)
            )

            AsyncImage(
                modifier = Modifier.size(75.dp),
                model = forecast.currentWeather.iconUrl,
                contentDescription = ""
            )
        }

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = forecast.currentWeather.date.formattedFullDate(),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.weight(1f))

        UpcomingWeatherBlock(
            weather = forecast.upcomingWeather
        )

        Spacer(modifier = Modifier.weight(0.5f))
    }
}
