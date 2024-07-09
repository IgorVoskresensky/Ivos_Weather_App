package com.ivos.domain.entities

import java.util.Calendar

data class Weather(
    val tempC: Double,
    val description: String,
    val iconUrl: String,
    val date: Calendar,
)
