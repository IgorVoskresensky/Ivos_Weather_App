package com.ivos.domain.usecases.details

import com.ivos.domain.repositories.DetailsRepo
import com.ivos.domain.repositories.FavoritesRepo
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val repo: DetailsRepo,
) {

    suspend operator fun invoke(cityId: Int) = repo.getWeather(cityId)
}
