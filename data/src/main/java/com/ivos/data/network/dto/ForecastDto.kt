package com.ivos.data.network.dto

import com.google.gson.annotations.SerializedName

data class ForecastDto(
    @SerializedName("forecastday")
    val days: List<ForecastDayDto> = emptyList()
)
