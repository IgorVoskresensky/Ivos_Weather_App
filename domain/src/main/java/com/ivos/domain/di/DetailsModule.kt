package com.ivos.domain.di

import com.ivos.domain.repositories.DetailsRepo
import com.ivos.domain.usecases.details.GetForecastUseCase
import com.ivos.domain.usecases.details.GetWeatherUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@[Module InstallIn(ViewModelComponent::class)]
class DetailsModule {

    @Provides
    fun providesGetForecastUseCase(repo: DetailsRepo) = GetForecastUseCase(repo)

    @Provides
    fun providesGetWeatherUseCase(repo: DetailsRepo) = GetWeatherUseCase(repo)
}
