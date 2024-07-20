package com.ivos.common.di

import com.ivos.common.network.NetworkManager
import com.ivos.common.utils.AppContextProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
class NetworkManagerModule {

    @[Provides Singleton]
    fun provideNetworkManager(contextProvider: AppContextProvider): NetworkManager {
        return NetworkManager(contextProvider)
    }
}
