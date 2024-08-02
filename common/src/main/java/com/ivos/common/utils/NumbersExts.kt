package com.ivos.common.utils

import java.util.Calendar
import java.util.Date
import kotlin.math.roundToInt

fun Long.toCalendar() = Calendar.getInstance().apply {
    time = Date(this@toCalendar * 1000)
}

fun Double.formatTempToString() = "${roundToInt()}°C"
