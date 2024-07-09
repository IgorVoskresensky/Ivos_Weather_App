package com.ivos.data.network.dto

import com.google.gson.annotations.SerializedName

data class WeatherForecastDto(
    @SerializedName("current")
    val current: WeatherDto = WeatherDto(),
    @SerializedName("forecast")
    val forecast: ForecastDto = ForecastDto(),
)
