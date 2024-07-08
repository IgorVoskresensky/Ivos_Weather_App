package com.ivos.domain.entities

data class Forecast(
    val currentWeather: Weather,
    val upcomingWeather: List<Weather>
)
