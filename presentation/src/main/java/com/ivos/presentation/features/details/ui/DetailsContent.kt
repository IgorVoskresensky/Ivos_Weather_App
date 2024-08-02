package com.ivos.presentation.features.details.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.ivos.presentation.features.details.component.DetailsComponent
import com.ivos.presentation.features.details.store.DetailsStore
import com.ivos.presentation.ui.sharedUi.FullScreenProgress

@Composable
fun DetailsContent(
    modifier: Modifier = Modifier,
    component: DetailsComponent,
) {
    val state by component.model.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        backgroundColor = MaterialTheme.colorScheme.background,
        topBar = {
            DetailsTopBar(
                cityName = state.city.name,
                isFavorite = state.isFavorite,
                onBack = { component.onClickBack() },
                onChangeFavoriteStatus = { component.onClickAddToFavorites(state.city) }
            )
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            when (val forecastState = state.forecastState) {
                is DetailsStore.State.ForecastState.Error -> {
                    ErrorScreen()
                }
                is DetailsStore.State.ForecastState.Initial -> {
                    InitialScreen()
                }
                is DetailsStore.State.ForecastState.Loading -> {
                    FullScreenProgress()
                }
                is DetailsStore.State.ForecastState.Success -> {
                    DetailsContentScreen(forecast = forecastState.forecast)
                }
            }
        }
    }
}
