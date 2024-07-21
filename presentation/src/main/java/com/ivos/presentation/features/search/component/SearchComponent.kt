package com.ivos.presentation.features.search.component

import com.arkivanov.decompose.ComponentContext
import com.ivos.presentation.features.search.store.SearchStoreFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

interface SearchComponent{

}

class SearchComponentImpl @AssistedInject constructor(
    private val storeFactory: SearchStoreFactory,
    @Assisted("componentContext") componentContext: ComponentContext,
) : SearchComponent, ComponentContext by componentContext {

    /*private val store = instanceKeeper.getStore { storeFactory.create() }*/

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
        ): SearchComponentImpl
    }
}
