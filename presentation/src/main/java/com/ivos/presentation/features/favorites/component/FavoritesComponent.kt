package com.ivos.presentation.features.favorites.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.ivos.domain.entities.City
import com.ivos.presentation.features.favorites.store.FavoritesStore
import com.ivos.presentation.features.favorites.store.FavoritesStore.Intent
import com.ivos.presentation.features.favorites.store.FavoritesStore.Label
import com.ivos.presentation.features.favorites.store.FavoritesStoreFactory
import com.ivos.presentation.utiles.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

interface FavoritesComponent {

    val model: StateFlow<FavoritesStore.State>

    fun onClickSearch()

    fun onClickAddToFavorites()

    fun onClickCity(city: City)
}

class FavoritesComponentImpl @AssistedInject constructor(
    private val storeFactory: FavoritesStoreFactory,
    @Assisted("onCityItemClick") onCityItemClick: (city: City) -> Unit,
    @Assisted("onAddFavoritesClick") onAddFavoritesClick: () -> Unit,
    @Assisted("onSearchClick") onSearchClick: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext,
) : FavoritesComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentScope()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model = store.stateFlow

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is Label.ClickAddToFavorites -> onAddFavoritesClick()
                    is Label.ClickOnCityItem -> onCityItemClick(it.city)
                    is Label.ClickSearch -> onSearchClick()
                }
            }
        }
    }

    override fun onClickSearch() = store.accept(Intent.ClickSearch)

    override fun onClickAddToFavorites() = store.accept(Intent.ClickAddToFavorites)

    override fun onClickCity(city: City) = store.accept(Intent.ClickOnCityItem(city))

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onCityItemClick") onCityItemClick: (city: City) -> Unit,
            @Assisted("onAddFavoritesClick") onAddFavoritesClick: () -> Unit,
            @Assisted("onSearchClick") onSearchClick: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext,
        ): FavoritesComponentImpl
    }
}

