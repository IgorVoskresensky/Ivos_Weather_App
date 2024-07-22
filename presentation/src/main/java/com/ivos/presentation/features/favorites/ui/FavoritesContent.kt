package com.ivos.presentation.features.favorites.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ivos.presentation.features.favorites.component.FavoritesComponent

@Composable
fun FavoritesContent(
    modifier: Modifier = Modifier,
    component: FavoritesComponent,
) {

    Box(modifier = Modifier.size(300.dp).background(Color.Red))
}
