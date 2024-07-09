package com.ivos.data.retrofitApi

import com.ivos.data.dto.CityDto
import com.ivos.data.dto.WeatherCurrentDto
import com.ivos.data.dto.WeatherForecastDto
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApiService {

    @GET("current.json?key")
    suspend fun loadCurrentWeather(
        @Query("q") query: String,
    ): WeatherCurrentDto

    @GET("forecast.json?key")
    suspend fun loadForecastWeather(
        @Query("q") query: String,
        @Query("days") daysCount: Int = 4,
    ): WeatherForecastDto

    @GET("search.json?key")
    suspend fun searchCity(
        @Query("q") query: String,
    ): List<CityDto>
}
