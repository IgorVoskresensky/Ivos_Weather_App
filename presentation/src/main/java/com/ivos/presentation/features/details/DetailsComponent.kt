package com.ivos.presentation.features.details

import com.arkivanov.decompose.ComponentContext

interface DetailsComponent {
}

class DetailsComponentImpl(
    componentContext: ComponentContext,
) : DetailsComponent, ComponentContext by componentContext {

}
