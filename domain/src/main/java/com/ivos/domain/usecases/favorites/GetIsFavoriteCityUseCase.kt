package com.ivos.domain.usecases.favorites

import com.ivos.domain.repositories.FavoritesRepo
import javax.inject.Inject

class GetIsFavoriteCityUseCase @Inject constructor(
    private val repo: FavoritesRepo,
) {

    operator fun invoke(cityId: Int) = repo.observeIsFavoriteCity(cityId)
}
