package com.ivos.presentation.features.favorites

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.ivos.domain.entities.City
import com.ivos.domain.usecases.details.GetWeatherUseCase
import com.ivos.domain.usecases.favorites.GetFavoriteCitiesUseCase
import com.ivos.presentation.features.favorites.FavoritesStore.Intent
import com.ivos.presentation.features.favorites.FavoritesStore.Intent.ClickAddToFavoritesIntent
import com.ivos.presentation.features.favorites.FavoritesStore.Intent.ClickOnCityItemIntent
import com.ivos.presentation.features.favorites.FavoritesStore.Intent.ClickSearchIntent
import com.ivos.presentation.features.favorites.FavoritesStore.Label
import com.ivos.presentation.features.favorites.FavoritesStore.Label.ClickAddToFavoritesLabel
import com.ivos.presentation.features.favorites.FavoritesStore.Label.ClickOnCityItemLabel
import com.ivos.presentation.features.favorites.FavoritesStore.Label.ClickSearchLabel
import com.ivos.presentation.features.favorites.FavoritesStore.State
import com.ivos.presentation.features.favorites.FavoritesStore.State.CityItem
import com.ivos.presentation.features.favorites.FavoritesStore.State.FavoritesWeatherState.Error
import com.ivos.presentation.features.favorites.FavoritesStore.State.FavoritesWeatherState.Initial
import com.ivos.presentation.features.favorites.FavoritesStore.State.FavoritesWeatherState.Loaded
import com.ivos.presentation.features.favorites.FavoritesStore.State.FavoritesWeatherState.Loading
import com.ivos.presentation.features.favorites.FavoritesStoreFactory.Action.FavoriteCitiesLoadedAction
import com.ivos.presentation.features.favorites.FavoritesStoreFactory.Msg.FavoriteCitiesLoadedMsg
import com.ivos.presentation.features.favorites.FavoritesStoreFactory.Msg.WeatherIsLoadingMsg
import com.ivos.presentation.features.favorites.FavoritesStoreFactory.Msg.WeatherLoadedErrorMsg
import com.ivos.presentation.features.favorites.FavoritesStoreFactory.Msg.WeatherLoadedMsg
import kotlinx.coroutines.launch
import javax.inject.Inject


interface FavoritesStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object ClickSearchIntent: Intent

        data object ClickAddToFavoritesIntent : Intent

        data class ClickOnCityItemIntent(val city: City) : Intent
    }

    data class State(
        val cityItems: List<CityItem>,
    ) {

        data class CityItem(
            val city: City,
            val weatherState: FavoritesWeatherState,
        )

        sealed interface FavoritesWeatherState {

            data object Initial : FavoritesWeatherState

            data object Loading : FavoritesWeatherState

            data object Error : FavoritesWeatherState

            data class Loaded(
                val tempC: Double,
                val iconUrl: String,
            ) : FavoritesWeatherState
        }
    }

    sealed interface Label {

        data object ClickSearchLabel: Label

        data object ClickAddToFavoritesLabel : Label

        data class ClickOnCityItemLabel(val city: City) : Label
    }
}

class FavoritesStoreFactory @Inject constructor(
    private val factory : StoreFactory,
    private val getFavoriteCitiesUseCase: GetFavoriteCitiesUseCase,
    private val getWeatherUseCase: GetWeatherUseCase,
) {
    fun create(): FavoritesStore =
        object : FavoritesStore, Store<Intent, State, Label> by factory.create(
            name = "FavoritesStore",
            initialState = State(listOf()),
            executorFactory = ::ExecutorImpl,
            bootstrapper = BootstrapperImpl(),
            reducer = ReducerImpl,
        ) {}

    private sealed interface Action {

        data class FavoriteCitiesLoadedAction(val cities: List<City>) : Action
    }

    private sealed interface Msg {

        data class FavoriteCitiesLoadedMsg(val cities: List<City>) : Msg

        data class WeatherLoadedMsg(
            val cityId: Int,
            val tempC: Double,
            val iconUrl: String,
        ) : Msg

        data class WeatherLoadedErrorMsg(val cityId: Int) : Msg

        data class WeatherIsLoadingMsg(val cityId: Int) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {

        override fun invoke() {
            scope.launch {
                getFavoriteCitiesUseCase().collect {
                    dispatch(FavoriteCitiesLoadedAction(it))
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        override fun executeIntent(intent: Intent) {
            when (intent) {
                is ClickAddToFavoritesIntent -> {
                    publish(ClickAddToFavoritesLabel)
                }
                is ClickOnCityItemIntent -> {
                    publish(ClickOnCityItemLabel(intent.city))
                }
                is ClickSearchIntent -> {
                    publish(ClickSearchLabel)
                }
            }
        }

        override fun executeAction(action: Action) {
            when (action) {
                is FavoriteCitiesLoadedAction -> {
                    val cities = action.cities
                    dispatch(FavoriteCitiesLoadedMsg(cities))
                    cities.forEach {
                        scope.launch {
                            loadWeatherForCity(it)
                        }
                    }
                }
            }
        }

        private suspend fun loadWeatherForCity(city: City) {
            dispatch(WeatherIsLoadingMsg(city.id))
            try {
                with(getWeatherUseCase(city.id)) {
                    dispatch(
                        WeatherLoadedMsg(
                            cityId = city.id,
                            tempC = tempC,
                            iconUrl = iconUrl,
                        )
                    )
                }
            } catch (e: Exception) {
                dispatch(WeatherLoadedErrorMsg(city.id))
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {

        override fun State.reduce(msg: Msg) = when (msg) {
            is FavoriteCitiesLoadedMsg -> {
                copy(
                    cityItems = msg.cities.map {
                        CityItem(
                            city = it,
                            weatherState = Initial,
                        )
                    }
                )
            }
            is WeatherIsLoadingMsg -> {
                copy(
                    cityItems = cityItems.map {
                        if (it.city.id == msg.cityId) {
                            it.copy(weatherState = Loading)
                        } else { it }
                    }
                )
            }
            is WeatherLoadedErrorMsg -> {
                copy(
                    cityItems = cityItems.map {
                        if (it.city.id == msg.cityId) {
                            it.copy(
                                weatherState = Error,
                            )
                        } else { it }
                    }
                )
            }
            is WeatherLoadedMsg -> {
                copy(
                    cityItems = cityItems.map {
                        if (it.city.id == msg.cityId) {
                            it.copy(
                                weatherState = Loaded(
                                    tempC = msg.tempC,
                                    iconUrl = msg.iconUrl
                                )
                            )
                        } else { it }
                    }
                )
            }
        }
    }
}
