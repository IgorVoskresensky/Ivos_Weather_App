package com.ivos.presentation.features.favorites.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.ivos.presentation.features.favorites.store.FavoritesStoreFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

interface FavoritesComponent {
}

class FavoritesComponentImpl @AssistedInject constructor(
    private val storeFactory: FavoritesStoreFactory,
    @Assisted("componentContext") componentContext: ComponentContext,
) : FavoritesComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
        ): FavoritesComponentImpl
    }
}

