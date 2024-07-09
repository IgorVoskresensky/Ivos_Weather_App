package com.ivos.data.repositories

import com.ivos.data.local.dao.FavoriteCitiesDao
import com.ivos.data.mappers.toEntityList
import com.ivos.data.mappers.toModel
import com.ivos.domain.entities.City
import com.ivos.domain.repositories.FavoritesRepo
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoritesRepoImpl @Inject constructor(
    private val dao: FavoriteCitiesDao,
) : FavoritesRepo {

    override val favoriteCities = dao.datFavoriteCities().map { it.toEntityList() }

    override fun observeIsFavoriteCity(cityId: Int) = dao.observeCityIsFavorite(cityId)

    override suspend fun addCityToFavorites(city: City) = dao.addCityToFavorite(city.toModel())

    override suspend fun removeCityFromFavorites(cityId: Int) = dao.removeCityFromFavorite(cityId)
}
