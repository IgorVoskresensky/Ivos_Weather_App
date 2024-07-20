package com.ivos.common.utils

import java.util.Calendar
import java.util.Date

fun Long.toCalendar() = Calendar.getInstance().apply {
    time = Date(this@toCalendar * 1000)
}
