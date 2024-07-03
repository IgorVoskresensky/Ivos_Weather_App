package com.ivos.domain.repositories

import com.ivos.domain.entities.City
import com.ivos.domain.entities.Forecast
import com.ivos.domain.entities.Weather

interface DetailsRepo {

    suspend fun getWeather(cityId: Int): Weather

    suspend fun getForecast(cityId: Int): Forecast
}
