package com.ivos.domain.repositories

import com.ivos.domain.entities.City
import kotlinx.coroutines.flow.Flow

interface FavoritesRepo {

    val favoriteCities: Flow<City>

    fun observeIsFavoriteCity(cityId: Int): Flow<Boolean>

    suspend fun addCityToFavorites(city: City)

    suspend fun removeCityFromFavorites(cityId: Int)
}
