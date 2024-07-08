package com.ivos.domain.entities

import android.icu.util.Calendar

data class Weather(
    val tempC: Float,
    val description: String,
    val iconUrl: String,
    val date: Calendar,
)
