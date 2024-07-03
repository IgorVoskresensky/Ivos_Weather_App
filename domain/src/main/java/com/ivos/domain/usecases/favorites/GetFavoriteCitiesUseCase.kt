package com.ivos.domain.usecases.favorites

import com.ivos.domain.repositories.FavoritesRepo
import javax.inject.Inject

class GetFavoriteCitiesUseCase @Inject constructor(
    private val repo: FavoritesRepo,
) {

    operator fun invoke() = repo.favoriteCities
}
