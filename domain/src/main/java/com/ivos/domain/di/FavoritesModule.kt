package com.ivos.domain.di

import com.ivos.domain.repositories.FavoritesRepo
import com.ivos.domain.usecases.favorites.ChangeCityIsFavoriteStatusUseCase
import com.ivos.domain.usecases.favorites.GetCityIsFavoriteUseCase
import com.ivos.domain.usecases.favorites.GetFavoriteCitiesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@[Module InstallIn(ViewModelComponent::class)]
class FavoritesModule {

    @Provides
    fun provideChangeCityIsFavoriteStatusUseCase(
        repo: FavoritesRepo
    ) = ChangeCityIsFavoriteStatusUseCase(repo)

    @Provides
    fun provideGetCityIsFavoriteUseCase(
        repo: FavoritesRepo
    ) = GetCityIsFavoriteUseCase(repo)

    @Provides
    fun provideGetFavoriteCitiesUseCase(
        repo: FavoritesRepo
    ) = GetFavoriteCitiesUseCase(repo)
}
