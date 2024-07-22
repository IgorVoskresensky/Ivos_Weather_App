package com.ivos.presentation.root

import android.os.Parcelable
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.ivos.domain.entities.City
import com.ivos.presentation.features.details.component.DetailsComponent
import com.ivos.presentation.features.details.component.DetailsComponentImpl
import com.ivos.presentation.features.favorites.component.FavoritesComponent
import com.ivos.presentation.features.favorites.component.FavoritesComponentImpl
import com.ivos.presentation.features.search.component.SearchComponent
import com.ivos.presentation.features.search.component.SearchComponentImpl
import com.ivos.presentation.features.search.store.Mode
import com.ivos.presentation.features.search.store.Mode.ADD_TO_FAVORITES_MODE
import com.ivos.presentation.features.search.store.Mode.SEARCH_MODE
import com.ivos.presentation.root.RootComponent.Child.Details
import com.ivos.presentation.root.RootComponent.Child.Favorites
import com.ivos.presentation.root.RootComponent.Child.Search
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.parcelize.Parcelize

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {

        data class Favorites(val component: FavoritesComponent) : Child

        data class Details(val component: DetailsComponent) : Child

        data class Search(val component: SearchComponent) : Child
    }
}

class RootComponentImpl @AssistedInject constructor(
    private val detailsFactory: DetailsComponentImpl.Factory,
    private val favoritesFactory: FavoritesComponentImpl.Factory,
    private val searchFactory: SearchComponentImpl.Factory,
    @Assisted("componentContext") componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        serializer = null,
        initialConfiguration = Config.Favorite,
        handleBackButton = true,
        childFactory = ::child
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext,
    ): RootComponent.Child {
        return when(config) {
            is Config.Details -> Details(
                detailsFactory.create(
                    city = config.city,
                    onClickBack = { navigation.pop() },
                    componentContext = componentContext,
                )
            )
            is Config.Favorite -> Favorites(
                favoritesFactory.create(
                    onCityItemClick  = { navigation.push(Config.Details(it)) },
                    onAddFavoritesClick = { navigation.push(Config.Search(ADD_TO_FAVORITES_MODE)) },
                    onSearchClick = { navigation.push(Config.Search(SEARCH_MODE)) },
                    componentContext = componentContext,
                )
            )
            is Config.Search -> Search(
                searchFactory.create(
                    mode = config.mode,
                    onClickBack = { navigation.pop() },
                    onClickDetails = { navigation.push(Config.Details(it)) },
                    onClickSave = { navigation.pop() },
                    componentContext = componentContext,
                )
            )
        }
    }

    sealed interface Config : Parcelable {

        @Parcelize
        data object Favorite : Config

        @Parcelize
        data class Search(val mode: Mode) : Config

        @Parcelize
        data class Details(val city: City) : Config
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
        ): RootComponentImpl
    }
}
