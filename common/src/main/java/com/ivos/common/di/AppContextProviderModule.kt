package com.ivos.common.di

import android.content.Context
import com.ivos.common.utils.AppContextProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
class AppContextProviderModule {

    @[Provides Singleton]
    fun provideAppContextProvider(@ApplicationContext context: Context): AppContextProvider {
        return AppContextProvider(context)
    }
}
