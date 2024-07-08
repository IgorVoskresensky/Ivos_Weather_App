package com.ivos.presentation.features.favorites

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.ivos.presentation.features.favorites.FavoritesStore.*


internal interface FavoritesStore : Store<Intent, State, Label> {

    sealed interface Intent

    data class State(val todo: Unit)

    sealed interface Label
}

internal class FavoritesStoreFactory(
    private val factory : StoreFactory,
) {
    fun create(): FavoritesStore =
        object : FavoritesStore, Store<Intent, State, Label> by factory.create(
            name = "FavoritesStore",
            initialState = State(Unit),
            executorFactory = ::ExecutorImpl,
            bootstrapper = BootstrapperImpl(),
            reducer = ReducerImpl,
        ) {}

    private sealed interface Action

    private sealed interface Msg

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {}
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
            super.executeIntent(intent)
        }

        override fun executeAction(action: Action) {
            super.executeAction(action)
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg) = State(kotlin.Unit)
    }
}
