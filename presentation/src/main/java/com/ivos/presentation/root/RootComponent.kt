package com.ivos.presentation.root

import com.arkivanov.decompose.ComponentContext

interface RootComponent {
}

class RootComponentImpl(
    componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext {

}
