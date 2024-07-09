package com.ivos.data.network.dto

import com.google.gson.annotations.SerializedName

data class CityDto(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("country")
    val country: String = "",
)
