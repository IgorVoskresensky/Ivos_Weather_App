package com.ivos.presentation.features.search.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.ivos.domain.entities.City
import com.ivos.domain.usecases.favorites.ChangeCityIsFavoriteStatusUseCase
import com.ivos.domain.usecases.search.SearchCityUseCase
import com.ivos.presentation.features.search.store.SearchStore.Intent
import com.ivos.presentation.features.search.store.SearchStore.Label
import com.ivos.presentation.features.search.store.SearchStore.State
import com.ivos.presentation.features.search.store.SearchStore.State.SearchState.Empty
import com.ivos.presentation.features.search.store.SearchStore.State.SearchState.Error
import com.ivos.presentation.features.search.store.SearchStore.State.SearchState.Initial
import com.ivos.presentation.features.search.store.SearchStore.State.SearchState.Loading
import com.ivos.presentation.features.search.store.SearchStore.State.SearchState.Success
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

interface SearchStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data class ChangeQuery(val query: String) : Intent

        data class ClickCity(val city: City) : Intent

        data object ClickBack : Intent

        data object ClickSearch : Intent
    }

    data class State(
        val query: String = "",
        val searchState: SearchState = Initial,
    ) {

        sealed interface SearchState {

            data object Initial : SearchState

            data object Loading : SearchState

            data object Error : SearchState

            data object Empty : SearchState

            data class Success(val cities: List<City>) : SearchState
        }
    }

    sealed interface Label {

        data object ClickBack : Label

        data object ClickSave : Label

        data class ClickDetails(val city: City) : Label
    }
}

class SearchStoreFactory @Inject constructor(
    private val factory : StoreFactory,
    private val searchCityUseCase: SearchCityUseCase,
    private val changeCityIsFavoriteStatusUseCase: ChangeCityIsFavoriteStatusUseCase,
) {
    fun create(mode: Mode): SearchStore =
        object : SearchStore, Store<Intent, State, Label> by factory.create(
            name = "FavoritesStore",
            initialState = State(),
            executorFactory = { ExecutorImpl(mode) },
            bootstrapper = BootstrapperImpl(),
            reducer = ReducerImpl,
        ) {}

    private sealed interface Action

    private sealed interface Msg {

        data class ChangeQuery(val query: String) : Msg

        data object Loading : Msg

        data object Error : Msg

        data class CitiesLoaded(val cities: List<City>) : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() { /*no op*/ }
    }

    private inner class ExecutorImpl(
         private val mode: Mode,
    ) : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        private var job: Job? = null

        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.ChangeQuery -> {
                    dispatch(Msg.ChangeQuery(intent.query))
                }
                is Intent.ClickBack -> {

                }
                is Intent.ClickCity -> {
                    when (mode) {
                        Mode.ADD_TO_FAVORITES_MODE -> {
                            scope.launch {
                                changeCityIsFavoriteStatusUseCase(intent.city, true)
                                publish(Label.ClickSave)
                            }
                        }
                        Mode.SEARCH_MODE -> {
                            publish(Label.ClickDetails(intent.city))
                        }
                    }
                }
                is Intent.ClickSearch -> {
                    job?.cancel()
                    job = scope.launch {
                        dispatch(Msg.Loading)
                        try {
                            val cities = searchCityUseCase(state().query)
                            dispatch(Msg.CitiesLoaded(cities))
                        } catch (e: Exception) {
                            dispatch(Msg.Error)
                        }
                    }
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.ChangeQuery -> {
                copy(query = msg.query)
            }
            is Msg.CitiesLoaded -> {
                copy(searchState = if (msg.cities.isNotEmpty()) Success(msg.cities) else Empty)
            }
            is Msg.Error -> {
                copy(searchState = Error)
            }
            is Msg.Loading -> {
                copy(searchState = Loading)
            }
        }
    }
}

enum class Mode {
    ADD_TO_FAVORITES_MODE, SEARCH_MODE
}
