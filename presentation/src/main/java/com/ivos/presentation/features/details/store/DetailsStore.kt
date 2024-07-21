package com.ivos.presentation.features.details.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.ivos.domain.entities.City
import com.ivos.domain.entities.Forecast
import com.ivos.domain.usecases.details.GetForecastUseCase
import com.ivos.domain.usecases.favorites.ChangeCityIsFavoriteStatusUseCase
import com.ivos.domain.usecases.favorites.GetCityIsFavoriteUseCase
import com.ivos.presentation.features.details.store.DetailsStore.Intent
import com.ivos.presentation.features.details.store.DetailsStore.Label
import com.ivos.presentation.features.details.store.DetailsStore.State
import com.ivos.presentation.features.details.store.DetailsStore.State.ForecastState.Error
import com.ivos.presentation.features.details.store.DetailsStore.State.ForecastState.Initial
import com.ivos.presentation.features.details.store.DetailsStore.State.ForecastState.Loading
import com.ivos.presentation.features.details.store.DetailsStore.State.ForecastState.Success
import kotlinx.coroutines.launch
import javax.inject.Inject

interface DetailsStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object ClickBack : Intent

        data object ChangeFavoriteStatus : Intent
    }

    data class State(
        val city: City = City(),
        val isFavorite: Boolean = false,
        val forecastState: ForecastState = Initial,
    ) {

        sealed interface ForecastState {

            data object Initial : ForecastState

            data object Loading : ForecastState

            data object Error : ForecastState

            data class Success(val forecast: Forecast) : ForecastState
        }
    }

    sealed interface Label {

        data object ClickBack : Label
    }
}

class DetailsStoreFactory @Inject constructor(
    private val factory : StoreFactory,
    private val getForecastUseCase: GetForecastUseCase,
    private val changeCityIsFavoriteStatusUseCase: ChangeCityIsFavoriteStatusUseCase,
    private val getCityIsFavoriteUseCase: GetCityIsFavoriteUseCase,
) {
    fun create(city: City): DetailsStore =
        object : DetailsStore, Store<Intent, State, Label> by factory.create(
            name = "DetailsStore",
            initialState = State(),
            executorFactory = ::ExecutorImpl,
            bootstrapper = BootstrapperImpl(city),
            reducer = ReducerImpl,
        ) {}

    private sealed interface Action {

        data class FavoriteStatusChanged(val isFavorite: Boolean) : Action

        data class ForecastLoaded(val forecast: Forecast) : Action

        data object ForecastLoading : Action

        data object ForecastError : Action
    }

    private sealed interface Msg {

        data class FavoriteStatusChanged(val isFavorite: Boolean) : Msg

        data class ForecastLoaded(val forecast: Forecast) : Msg

        data object ForecastLoading : Msg

        data object ForecastError : Msg
    }

    private inner class BootstrapperImpl(val city: City) : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getCityIsFavoriteUseCase(city.id).collect {
                    dispatch(Action.FavoriteStatusChanged(it))
                }
            }

            scope.launch {
                dispatch(Action.ForecastLoading)
                try {
                    val forecast = getForecastUseCase(city.id)
                    dispatch(Action.ForecastLoaded(forecast))
                } catch (e: Exception) {
                    dispatch(Action.ForecastError)
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
            when (intent) {
                Intent.ChangeFavoriteStatus -> {
                    val state = state()
                    scope.launch {
                        changeCityIsFavoriteStatusUseCase(state.city, !state.isFavorite)
                    }
                }
                Intent.ClickBack -> {
                    publish(Label.ClickBack)
                }
            }
        }

        override fun executeAction(action: Action) {
            when (action) {
                is Action.FavoriteStatusChanged -> {
                    dispatch(Msg.FavoriteStatusChanged(action.isFavorite))
                }
                is Action.ForecastError -> {
                    dispatch(Msg.ForecastError)
                }
                is Action.ForecastLoaded -> {
                    dispatch(Msg.ForecastLoaded(action.forecast))
                }
                is Action.ForecastLoading -> {
                    dispatch(Msg.ForecastLoading)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.FavoriteStatusChanged -> {
                copy(isFavorite = msg.isFavorite)
            }
            is Msg.ForecastError -> {
                copy(forecastState = Error)
            }
            is Msg.ForecastLoaded -> {
                copy(forecastState = Success(msg.forecast))
            }
            is Msg.ForecastLoading -> {
                copy(forecastState = Loading)
            }
        }
    }
}
