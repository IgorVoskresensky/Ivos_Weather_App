package com.ivos.data.repositories

import com.ivos.data.mappers.toEntity
import com.ivos.data.network.retrofitApi.RetrofitApiService
import com.ivos.domain.repositories.SearchRepo
import javax.inject.Inject

class SearchRepoImpl @Inject constructor(
    private val api: RetrofitApiService,
) : SearchRepo {

    override suspend fun searchCity(query: String) = api.searchCity(query).map { it.toEntity() }
}
