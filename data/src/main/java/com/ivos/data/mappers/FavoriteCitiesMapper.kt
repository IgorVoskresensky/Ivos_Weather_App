package com.ivos.data.mappers

import com.ivos.data.local.model.CityModel
import com.ivos.domain.entities.City

fun City.toModel() = CityModel(id, name, country)

fun CityModel.toEntity() = City(id, name, country)

fun List<CityModel>.toEntityList() = map { it.toEntity() }
