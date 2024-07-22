package com.ivos.presentation.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.ivos.presentation.features.details.ui.DetailsContent
import com.ivos.presentation.features.favorites.ui.FavoritesContent
import com.ivos.presentation.features.search.ui.SearchContent
import com.ivos.presentation.ui.theme.IvosWeatherAppTheme

@Composable
fun RootContent(
    component: RootComponent,
) {
    IvosWeatherAppTheme {
        Children(stack = component.stack) {
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
}
