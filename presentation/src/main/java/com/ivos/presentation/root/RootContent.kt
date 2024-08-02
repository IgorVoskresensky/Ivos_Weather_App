package com.ivos.presentation.root

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.ivos.presentation.features.details.ui.DetailsContent
import com.ivos.presentation.features.favorites.ui.FavoritesContent
import com.ivos.presentation.features.search.ui.SearchContent

@Composable
fun RootContent(
    paddingValues: PaddingValues,
    component: RootComponent,
) {
    Children(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        stack = component.stack,
    ) {
        when (val instance = it.instance) {
            is RootComponent.Child.Details -> {
                DetailsContent(component = instance.component)
            }
            is RootComponent.Child.Favorites -> {
                FavoritesContent(component = instance.component)
            }
            is RootComponent.Child.Search -> {
                SearchContent(component = instance.component)
            }
        }
    }
}
