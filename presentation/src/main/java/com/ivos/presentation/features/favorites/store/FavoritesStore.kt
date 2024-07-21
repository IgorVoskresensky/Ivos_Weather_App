package com.ivos.presentation.features.favorites.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.ivos.domain.entities.City
import com.ivos.domain.usecases.details.GetWeatherUseCase
import com.ivos.domain.usecases.favorites.GetFavoriteCitiesUseCase
import com.ivos.presentation.features.favorites.store.FavoritesStore.Intent
import com.ivos.presentation.features.favorites.store.FavoritesStore.Label
import com.ivos.presentation.features.favorites.store.FavoritesStore.State
import com.ivos.presentation.features.favorites.store.FavoritesStore.State.CityItem
import com.ivos.presentation.features.favorites.store.FavoritesStore.State.FavoritesWeatherState.Error
import com.ivos.presentation.features.favorites.store.FavoritesStore.State.FavoritesWeatherState.Initial
import com.ivos.presentation.features.favorites.store.FavoritesStore.State.FavoritesWeatherState.Loading
import com.ivos.presentation.features.favorites.store.FavoritesStore.State.FavoritesWeatherState.Success
import kotlinx.coroutines.launch
import javax.inject.Inject


interface FavoritesStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object ClickSearch : Intent

        data object ClickAddToFavorites : Intent

        data class ClickOnCityItem(val city: City) : Intent
    }

    data class State(
        val cityItems: List<CityItem> = listOf(),
    ) {

        data class CityItem(
            val city: City = City(),
            val weatherState: FavoritesWeatherState = Initial,
        )

        sealed interface FavoritesWeatherState {

            data object Initial : FavoritesWeatherState

            data object Loading : FavoritesWeatherState

            data object Error : FavoritesWeatherState

            data class Success(
                val tempC: Double,
                val iconUrl: String,
            ) : FavoritesWeatherState
        }
    }

    sealed interface Label {

        data object ClickSearch : Label

        data object ClickAddToFavorites : Label

        data class ClickOnCityItem(val city: City) : Label
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
            initialState = State(),
            executorFactory = ::ExecutorImpl,
            bootstrapper = BootstrapperImpl(),
            reducer = ReducerImpl,
        ) {}

    private sealed interface Action {

        data class FavoriteCitiesLoaded(val cities: List<City>) : Action
    }

    private sealed interface Msg {

        data class FavoriteCitiesLoaded(val cities: List<City>) : Msg

        data class WeatherLoaded(
            val cityId: Int,
            val tempC: Double,
            val iconUrl: String,
        ) : Msg

        data class WeatherLoadedError(val cityId: Int) : Msg

        data class WeatherIsLoading(val cityId: Int) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getFavoriteCitiesUseCase().collect {
                    dispatch(Action.FavoriteCitiesLoaded(it))
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.ClickAddToFavorites -> {
                    publish(Label.ClickAddToFavorites)
                }
                is Intent.ClickOnCityItem -> {
                    publish(Label.ClickOnCityItem(intent.city))
                }
                is Intent.ClickSearch -> {
                    publish(Label.ClickSearch)
                }
            }
        }

        override fun executeAction(action: Action) {
            when (action) {
                is Action.FavoriteCitiesLoaded -> {
                    val cities = action.cities
                    dispatch(Msg.FavoriteCitiesLoaded(cities))
                    cities.forEach {
                        scope.launch {
                            loadWeatherForCity(it)
                        }
                    }
                }
            }
        }

        private suspend fun loadWeatherForCity(city: City) {
            dispatch(Msg.WeatherIsLoading(city.id))
            try {
                with(getWeatherUseCase(city.id)) {
                    dispatch(
                        Msg.WeatherLoaded(
                            cityId = city.id,
                            tempC = tempC,
                            iconUrl = iconUrl,
                        )
                    )
                }
            } catch (e: Exception) {
                dispatch(Msg.WeatherLoadedError(city.id))
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg) = when (msg) {
            is Msg.FavoriteCitiesLoaded -> {
                copy(
                    cityItems = msg.cities.map {
                        CityItem(
                            city = it,
                            weatherState = Initial,
                        )
                    }
                )
            }
            is Msg.WeatherIsLoading -> {
                copy(
                    cityItems = cityItems.map {
                        if (it.city.id == msg.cityId) {
                            it.copy(weatherState = Loading)
                        } else { it }
                    }
                )
            }
            is Msg.WeatherLoadedError -> {
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
            is Msg.WeatherLoaded -> {
                copy(
                    cityItems = cityItems.map {
                        if (it.city.id == msg.cityId) {
                            it.copy(
                                weatherState = Success(
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
