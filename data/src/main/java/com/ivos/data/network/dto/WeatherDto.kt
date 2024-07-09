package com.ivos.data.network.dto

import com.google.gson.annotations.SerializedName

data class WeatherDto(
    @SerializedName("last_updated_epoch")
    val lastUpdate: Long = 0L,
    @SerializedName("temp_c")
    val tempC: Double = 0.0,
    @SerializedName("condition")
    val condition: ConditionDto = ConditionDto(),
)
