package com.ivos.data.di

import com.ivos.common.utils.AppContextProvider
import com.ivos.data.local.IvosWeatherDB
import com.ivos.data.network.retrofitApi.ApiFactory
import com.ivos.data.network.retrofitApi.RetrofitApiService
import com.ivos.data.repositories.DetailsRepoImpl
import com.ivos.data.repositories.FavoritesRepoImpl
import com.ivos.data.repositories.SearchRepoImpl
import com.ivos.domain.repositories.DetailsRepo
import com.ivos.domain.repositories.FavoritesRepo
import com.ivos.domain.repositories.SearchRepo
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@[Module InstallIn(SingletonComponent::class)]
interface DataModule {

    @Binds
    fun bindFavoriteRepository(impl: FavoritesRepoImpl): FavoritesRepo

    @Binds
    fun bindDetailsRepository(impl: DetailsRepoImpl): DetailsRepo

    @Binds
    fun bindSearchRepository(impl: SearchRepoImpl): SearchRepo

    companion object {

        @Provides
        fun provideApiService(): RetrofitApiService = ApiFactory.retrofitApiService

        @Provides
        fun provideDatabase(
            contextProvider: AppContextProvider
        ) = IvosWeatherDB.getInstance(contextProvider.context)

        @Provides
        fun provideFavoriteCitiesDai(db: IvosWeatherDB) = db.favoriteCitiesDao()
    }
}
