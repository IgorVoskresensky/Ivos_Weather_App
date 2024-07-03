package com.ivos.domain.usecases.favorites

import com.ivos.domain.entities.City
import com.ivos.domain.repositories.FavoritesRepo
import javax.inject.Inject

class AddCityToFavoritesUseCase @Inject constructor(
    private val repo: FavoritesRepo,
) {

    suspend operator fun invoke(city: City) = repo.addCityToFavorites(city)
}
