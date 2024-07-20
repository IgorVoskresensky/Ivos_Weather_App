package com.ivos.domain.di

import com.ivos.domain.repositories.SearchRepo
import com.ivos.domain.usecases.search.SearchCityUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@[Module InstallIn(ViewModelComponent::class)]
class SearchModule {

    @Provides
    fun providesSearchCityUseCase(repo: SearchRepo) = SearchCityUseCase(repo)
}
