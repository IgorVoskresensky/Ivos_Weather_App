package com.ivos.data.mappers

import com.ivos.common.PROTOCOL
import com.ivos.common.toCalendar
import com.ivos.data.network.dto.ForecastDayDto
import com.ivos.data.network.dto.WeatherCurrentDto
import com.ivos.data.network.dto.WeatherDto
import com.ivos.data.network.dto.WeatherForecastDto
import com.ivos.domain.entities.Forecast
import com.ivos.domain.entities.Weather

fun WeatherCurrentDto.toEntity() = current.toEntity()

fun WeatherDto.toEntity() = Weather(
    tempC = tempC,
    description = condition.text,
    iconUrl = condition.iconUrl.updateIconUrl(),
    date = lastUpdate.toCalendar(),
)

fun WeatherForecastDto.toEntity() = Forecast(
    currentWeather = current.toEntity(),
    upcomingWeather = forecast.days.drop(1).map { it.toEntity() } //first day removed by drop
)

fun ForecastDayDto.toEntity() = with(day) { Weather(
    tempC = tempC,
    description = condition.text,
    iconUrl = condition.iconUrl,
    date = dateEpoch.toCalendar(),
) }

private fun String.updateIconUrl() = "$PROTOCOL$this".replace("64x64", "128x128")
