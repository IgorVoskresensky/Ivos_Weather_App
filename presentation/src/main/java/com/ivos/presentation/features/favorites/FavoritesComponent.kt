package com.ivos.presentation.features.favorites

import com.arkivanov.decompose.ComponentContext

interface FavoritesComponent {
}

class FavoritesComponentImpl(
    componentContext: ComponentContext,
) : FavoritesComponent, ComponentContext by componentContext {

}

