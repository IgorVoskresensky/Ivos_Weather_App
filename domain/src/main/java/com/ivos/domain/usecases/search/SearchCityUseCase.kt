package com.ivos.domain.usecases.search

import com.ivos.domain.repositories.SearchRepo
import javax.inject.Inject

class SearchCityUseCase @Inject constructor(
    private val repo: SearchRepo,
) {

    suspend operator fun invoke(query: String) = repo.searchCity(query)
}
