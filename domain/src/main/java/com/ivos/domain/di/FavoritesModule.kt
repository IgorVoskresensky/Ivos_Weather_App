package com.ivos.domain.di

import com.ivos.domain.repositories.FavoritesRepo
import com.ivos.domain.usecases.favorites.AddCityToFavoritesUseCase
import com.ivos.domain.usecases.favorites.GetCityIsFavoriteUseCase
import com.ivos.domain.usecases.favorites.GetFavoriteCitiesUseCase
import com.ivos.domain.usecases.favorites.RemoveCityFromFavoritesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@[Module InstallIn(ViewModelComponent::class)]
class FavoritesModule {

    @Provides
    fun provideAddCityToFavoritesUseCase(
        repo: FavoritesRepo
    ) = AddCityToFavoritesUseCase(repo)

    @Provides
    fun provideGetCityIsFavoriteUseCase(
        repo: FavoritesRepo
    ) = GetCityIsFavoriteUseCase(repo)

    @Provides
    fun provideGetFavoriteCitiesUseCase(
        repo: FavoritesRepo
    ) = GetFavoriteCitiesUseCase(repo)

    @Provides
    fun provideRemoveCityFromFavoritesUseCase(
        repo: FavoritesRepo
    ) = RemoveCityFromFavoritesUseCase(repo)
}
