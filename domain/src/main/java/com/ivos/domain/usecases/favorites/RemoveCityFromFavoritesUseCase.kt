package com.ivos.domain.usecases.favorites

import com.ivos.domain.repositories.FavoritesRepo
import javax.inject.Inject

class RemoveCityFromFavoritesUseCase @Inject constructor(
    private val repo: FavoritesRepo,
) {

    suspend operator fun invoke(cityId: Int) = repo.removeCityFromFavorites(cityId)
}
