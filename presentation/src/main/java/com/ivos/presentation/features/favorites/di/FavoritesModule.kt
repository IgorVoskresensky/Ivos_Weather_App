package com.ivos.presentation.features.favorites.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@[Module InstallIn(ViewModelComponent::class)]
class FavoritesModule {

    @Provides
    fun provideStoreFactory(): StoreFactory = DefaultStoreFactory()
}
