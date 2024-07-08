package com.ivos.presentation.features.search

import com.arkivanov.decompose.ComponentContext

interface SearchComponent{

}

class SearchComponentImpl(
    componentContext: ComponentContext,
) : SearchComponent, ComponentContext by componentContext {
    
}
