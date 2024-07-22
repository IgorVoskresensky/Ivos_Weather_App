package com.ivos.presentation.features.favorites.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ivos.common.utils.formatTempToString
import com.ivos.presentation.features.favorites.component.FavoritesComponent
import com.ivos.presentation.features.favorites.store.FavoritesStore
import com.ivos.presentation.features.favorites.store.FavoritesStore.State.CityItem
import com.ivos.presentation.utiles.Gradients

@Composable
fun FavoritesContent(
    modifier: Modifier = Modifier,
    component: FavoritesComponent,
) {
    val state by component.model.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyVerticalGrid(
            modifier = modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth(),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(
                items = state.cityItems,
                key = { it.city.id }
            ) {
                FavoriteItemCard(item = it)
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .padding(end = 32.dp, bottom = 56.dp)
                .align(Alignment.BottomEnd)
                .size(80.dp)
                .clip(CircleShape),
            onClick = { component.onClickAddToFavorites() }
        ) {
            Text(
                text = "+",
                fontSize = 48.sp
            )
        }
    }
}

@Composable
fun FavoriteItemCard(
    modifier: Modifier = Modifier,
    item: CityItem,
) {
    Card(
        modifier = modifier
            .size(150.dp)
            .clip(MaterialTheme.shapes.medium)
            .shadow(
                elevation = 10.dp,
                shape = MaterialTheme.shapes.medium,
                spotColor = Gradients.night.shadow
            ),
        border = BorderStroke(2.dp, Gradients.night.secondary),
    ) {
        when (item.weatherState) {
            is FavoritesStore.State.FavoritesWeatherState.Error -> {}
            is FavoritesStore.State.FavoritesWeatherState.Initial -> {}
            is FavoritesStore.State.FavoritesWeatherState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Gradients.night.primary),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is FavoritesStore.State.FavoritesWeatherState.Success -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Gradients.night.primary)
                        .drawBehind {
                            drawCircle(
                                brush = Gradients.night.secondary,
                                radius = size.minDimension / 2.5f
                            )
                        }
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                modifier = Modifier,
                                text = item.city.name,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleMedium,
                            )

                            AsyncImage(
                                modifier = Modifier
                                    .size(56.dp),
                                model = item.weatherState.iconUrl,
                                contentDescription = ""
                            )
                        }

                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = item.weatherState.tempC.formatTempToString(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }
            }
        }
    }
}
