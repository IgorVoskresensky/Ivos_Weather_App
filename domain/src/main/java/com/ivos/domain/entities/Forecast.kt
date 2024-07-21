package com.ivos.domain.entities

data class Forecast(
    val currentWeather: Weather = Weather(),
    val upcomingWeather: List<Weather> = listOf(),
)
