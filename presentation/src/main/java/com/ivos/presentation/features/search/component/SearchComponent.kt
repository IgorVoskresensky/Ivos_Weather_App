package com.ivos.presentation.features.search.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.ivos.domain.entities.City
import com.ivos.presentation.features.search.store.Mode
import com.ivos.presentation.features.search.store.SearchStore
import com.ivos.presentation.features.search.store.SearchStore.Intent
import com.ivos.presentation.features.search.store.SearchStore.Label
import com.ivos.presentation.features.search.store.SearchStoreFactory
import com.ivos.presentation.utiles.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

interface SearchComponent{

    val model: StateFlow<SearchStore.State>

    fun changeQuery(query: String)

    fun onClickBack()

    fun onClickSearch()

    fun onClickCity(city: City)
}

class SearchComponentImpl @AssistedInject constructor(
    private val storeFactory: SearchStoreFactory,
    @Assisted("mode") mode: Mode,
    @Assisted("onClickBack") onClickBack: () -> Unit,
    @Assisted("onClickDetails") onClickDetails: (City) -> Unit,
    @Assisted("onClickSave") onClickSave: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext,
) : SearchComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create(mode) }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is Label.ClickBack -> onClickBack()
                    is Label.ClickDetails -> onClickDetails(it.city)
                    is Label.ClickSave -> onClickSave()
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model = store.stateFlow

    override fun changeQuery(query: String) = store.accept(Intent.ChangeQuery(query))

    override fun onClickBack() = store.accept(Intent.ClickBack)

    override fun onClickSearch() = store.accept(Intent.ClickSearch)

    override fun onClickCity(city: City) = store.accept(Intent.ClickCity(city))

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("mode") mode: Mode,
            @Assisted("onClickBack") onClickBack: () -> Unit,
            @Assisted("onClickDetails") onClickDetails: (City) -> Unit,
            @Assisted("onClickSave") onClickSave: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext,
        ): SearchComponentImpl
    }
}
