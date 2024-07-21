package com.ivos.domain.entities

import java.util.Calendar
import java.util.Date

data class Weather(
    val tempC: Double = 0.0,
    val description: String = "",
    val iconUrl: String = "",
    val date: Calendar = Calendar.getInstance().apply { time = Date() },
)
