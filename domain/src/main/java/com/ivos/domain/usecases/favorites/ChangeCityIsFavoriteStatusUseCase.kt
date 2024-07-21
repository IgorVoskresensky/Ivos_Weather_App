package com.ivos.domain.usecases.favorites

import com.ivos.domain.entities.City
import com.ivos.domain.repositories.FavoritesRepo
import javax.inject.Inject

class ChangeCityIsFavoriteStatusUseCase @Inject constructor(
    private val repo: FavoritesRepo,
) {

    suspend operator fun invoke(
        city: City,
        isFavorite: Boolean,
    ) = if (isFavorite) repo.addCityToFavorites(city) else repo.removeCityFromFavorites(city.id)
}
