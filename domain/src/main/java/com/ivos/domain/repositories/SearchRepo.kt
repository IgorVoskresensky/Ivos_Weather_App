package com.ivos.domain.repositories

import com.ivos.domain.entities.City

interface SearchRepo {

    suspend fun searchCity(query: String): List<City>
}
