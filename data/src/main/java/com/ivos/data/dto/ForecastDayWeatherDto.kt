package com.ivos.data.dto

import com.google.gson.annotations.SerializedName

data class ForecastDayWeatherDto(
    @SerializedName("avgtemp_c")
    val tempC: Double = 0.0,
    @SerializedName("condition")
    val condition: ConditionDto = ConditionDto(),
)
