package com.ivos.presentation.features.details.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.ivos.domain.entities.City
import com.ivos.presentation.features.details.store.DetailsStore
import com.ivos.presentation.features.details.store.DetailsStore.Intent
import com.ivos.presentation.features.details.store.DetailsStore.Label
import com.ivos.presentation.features.details.store.DetailsStoreFactory
import com.ivos.presentation.utiles.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

interface DetailsComponent {

    val model: StateFlow<DetailsStore.State>

    fun onClickBack()

    fun onClickAddToFavorites(city: City)
}

class DetailsComponentImpl @AssistedInject constructor(
    private val storeFactory: DetailsStoreFactory,
    @Assisted("city") private val city: City,
    @Assisted("onClickBack") onClickBack: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext,
) : DetailsComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create(city) }
    private val scope = componentScope()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model = store.stateFlow

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    Label.ClickBack -> onClickBack()
                }
            }
        }
    }

    override fun onClickBack() = store.accept(Intent.ClickBack)

    override fun onClickAddToFavorites(city: City) = store.accept(Intent.ChangeFavoriteStatus)

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("city") city: City,
            @Assisted("onClickBack") onClickBack: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext,
        ): DetailsComponentImpl
    }
}
