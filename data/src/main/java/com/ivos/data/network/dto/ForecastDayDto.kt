package com.ivos.data.network.dto

import com.google.gson.annotations.SerializedName

data class ForecastDayDto(
    @SerializedName("date_epoch")
    val dateEpoch: Long = 0L,
    @SerializedName("day")
    val day: ForecastDayWeatherDto = ForecastDayWeatherDto(),
)
