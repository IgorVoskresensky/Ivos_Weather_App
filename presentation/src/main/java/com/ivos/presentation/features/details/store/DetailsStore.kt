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
import com.ivos.presentation.features.details.store.DetailsStore.Label.ClickBackLabel
import com.ivos.presentation.features.details.store.DetailsStore.State
import com.ivos.presentation.features.details.store.DetailsStore.State.ForecastState.Error
import com.ivos.presentation.features.details.store.DetailsStore.State.ForecastState.Initial
import com.ivos.presentation.features.details.store.DetailsStore.State.ForecastState.Loading
import com.ivos.presentation.features.details.store.DetailsStore.State.ForecastState.Success
import com.ivos.presentation.features.details.store.DetailsStoreFactory.Action.FavoriteStatusChangedAction
import com.ivos.presentation.features.details.store.DetailsStoreFactory.Action.ForecastErrorAction
import com.ivos.presentation.features.details.store.DetailsStoreFactory.Action.ForecastLoadedAction
import com.ivos.presentation.features.details.store.DetailsStoreFactory.Action.ForecastLoadingAction
import com.ivos.presentation.features.details.store.DetailsStoreFactory.Msg.FavoriteStatusChangedMsg
import com.ivos.presentation.features.details.store.DetailsStoreFactory.Msg.ForecastErrorMsg
import com.ivos.presentation.features.details.store.DetailsStoreFactory.Msg.ForecastLoadedMsg
import com.ivos.presentation.features.details.store.DetailsStoreFactory.Msg.ForecastLoadingMsg
import kotlinx.coroutines.launch
import javax.inject.Inject

interface DetailsStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object ClickBackIntent : Intent

        data object ChangeFavoriteStatusIntent : Intent
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

        data object ClickBackLabel : Label
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

        data class FavoriteStatusChangedAction(val isFavorite: Boolean) : Action

        data class ForecastLoadedAction(val forecast: Forecast) : Action

        data object ForecastLoadingAction : Action

        data object ForecastErrorAction : Action
    }

    private sealed interface Msg {

        data class FavoriteStatusChangedMsg(val isFavorite: Boolean) : Msg

        data class ForecastLoadedMsg(val forecast: Forecast) : Msg

        data object ForecastLoadingMsg : Msg

        data object ForecastErrorMsg : Msg
    }

    private inner class BootstrapperImpl(val city: City) : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getCityIsFavoriteUseCase(city.id).collect {
                    dispatch(FavoriteStatusChangedAction(it))
                }
            }

            scope.launch {
                dispatch(ForecastLoadingAction)
                try {
                    val forecast = getForecastUseCase(city.id)
                    dispatch(ForecastLoadedAction(forecast))
                } catch (e: Exception) {
                    dispatch(ForecastErrorAction)
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
            when (intent) {
                Intent.ChangeFavoriteStatusIntent -> {
                    val state = state()
                    scope.launch {
                        changeCityIsFavoriteStatusUseCase(state.city, !state.isFavorite)
                    }
                }
                Intent.ClickBackIntent -> {
                    publish(ClickBackLabel)
                }
            }
        }

        override fun executeAction(action: Action) {
            when (action) {
                is FavoriteStatusChangedAction -> {
                    dispatch(FavoriteStatusChangedMsg(action.isFavorite))
                }
                is ForecastErrorAction -> {
                    dispatch(ForecastErrorMsg)
                }
                is ForecastLoadedAction -> {
                    dispatch(ForecastLoadedMsg(action.forecast))
                }
                is ForecastLoadingAction -> {
                    dispatch(ForecastLoadingMsg)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is FavoriteStatusChangedMsg -> {
                copy(isFavorite = msg.isFavorite)
            }
            is ForecastErrorMsg -> {
                copy(forecastState = Error)
            }
            is ForecastLoadedMsg -> {
                copy(forecastState = Success(msg.forecast))
            }
            is ForecastLoadingMsg -> {
                copy(forecastState = Loading)
            }
        }
    }
}
