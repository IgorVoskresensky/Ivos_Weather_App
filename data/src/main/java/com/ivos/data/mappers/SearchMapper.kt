package com.ivos.data.mappers

import com.ivos.data.network.dto.CityDto
import com.ivos.domain.entities.City

fun CityDto.toEntity() = City(id, name, country)
