package com.ivos.data.repositories

import com.ivos.data.mappers.toEntity
import com.ivos.data.network.retrofitApi.RetrofitApiService
import com.ivos.domain.repositories.DetailsRepo
import javax.inject.Inject

class DetailsRepoImpl @Inject constructor(
    private val api: RetrofitApiService,
) : DetailsRepo {

    override suspend fun getWeather(cityId: Int) = api.loadCurrentWeather(
        "$PREFIX_CITY_ID$cityId"
    ).toEntity()

    override suspend fun getForecast(cityId: Int) = api.loadForecastWeather(
        "$PREFIX_CITY_ID$cityId"
    ).toEntity()

    private companion object {
        const val PREFIX_CITY_ID = ":id"
    }
}
