package com.ivos.common.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun Calendar.formattedFullDate() = SimpleDateFormat("EEEE | d m y", Locale.getDefault()).format(time)

fun Calendar.formattedShortDayOfWeekDate() = SimpleDateFormat("EEE", Locale.getDefault()).format(time)
