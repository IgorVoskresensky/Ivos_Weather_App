package com.ivos.presentation.features.details.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsTopBar(
    modifier: Modifier = Modifier,
    cityName: String,
    isFavorite: Boolean,
    onBack: () -> Unit,
    onChangeFavoriteStatus: () -> Unit,
) {
    println(cityName)
    CenterAlignedTopAppBar(
        modifier = modifier.padding(0.dp),
        title = {
            Text(
                text = cityName,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        ),
        navigationIcon = {
            IconButton(
                onClick = { onBack() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = "",
                )
            }
        },
        actions = {
            IconButton(
                onClick = { onChangeFavoriteStatus() }
            ) {
                Icon(
                    imageVector = if (isFavorite)
                        Icons.Default.Star else Icons.Default.StarBorder,
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = ""
                )
            }
        }
    )
}
