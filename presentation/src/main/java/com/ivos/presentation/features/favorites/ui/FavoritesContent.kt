package com.ivos.presentation.features.favorites.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import com.ivos.presentation.ui.sharedUi.FullScreenProgress
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
        Column(
            modifier.align(Alignment.TopCenter)
        ) {
            SearchField()

            LazyVerticalGrid(
                modifier = modifier.fillMaxWidth(),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(
                    items = state.cityItems,
                    key = { it.city.id }
                ) {
                    FavoriteItemCard(
                        item = it,
                        onClick = { component.onClickCity(it.city) }
                    )
                }
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .padding(end = 32.dp, bottom = 56.dp)
                .align(Alignment.BottomEnd)
                .size(80.dp)
                .clip(CircleShape),
            containerColor = MaterialTheme.colorScheme.onBackground,
            contentColor = MaterialTheme.colorScheme.background,
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
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .size(150.dp)
            .clip(MaterialTheme.shapes.medium)
            .shadow(
                elevation = 10.dp,
                shape = MaterialTheme.shapes.medium,
                spotColor = Gradients.night.shadow
            )
            .clickable { onClick() },
        border = BorderStroke(2.dp, Gradients.night.secondary),
    ) {
        when (item.weatherState) {
            is FavoritesStore.State.FavoritesWeatherState.Error -> {}
            is FavoritesStore.State.FavoritesWeatherState.Initial -> {}
            is FavoritesStore.State.FavoritesWeatherState.Loading -> {
                FullScreenProgress()
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

@Composable
fun SearchField(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(horizontal = 32.dp)
            .background(
                color = MaterialTheme.colorScheme.onBackground,
                shape = MaterialTheme.shapes.medium,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            modifier = Modifier.padding(start = 16.dp),
            imageVector = Icons.Default.Search,
            tint = MaterialTheme.colorScheme.background,
            contentDescription = ""
        )
        
        Text(
            modifier = modifier/*.padding(16.dp)*/,
            text = "Search",
            color = MaterialTheme.colorScheme.background,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
