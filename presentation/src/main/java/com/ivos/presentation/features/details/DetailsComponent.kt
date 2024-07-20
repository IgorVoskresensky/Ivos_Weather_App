package com.ivos.presentation.features.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

interface DetailsComponent {
}

class DetailsComponentImpl @AssistedInject constructor(
    private val storeFactory: DetailsStoreFactory,
    @Assisted("componentContext") componentContext: ComponentContext,
) : DetailsComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
        ): DetailsComponentImpl
    }
}
