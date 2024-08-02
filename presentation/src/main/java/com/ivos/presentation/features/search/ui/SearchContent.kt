package com.ivos.presentation.features.search.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ivos.presentation.features.details.ui.ErrorScreen
import com.ivos.presentation.features.details.ui.InitialScreen
import com.ivos.presentation.features.search.component.SearchComponent
import com.ivos.presentation.features.search.store.SearchStore
import com.ivos.presentation.ui.sharedUi.FullScreenProgress

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchContent(
    modifier: Modifier = Modifier,
    component: SearchComponent,
) {
    val state by component.model.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column {
            SearchBar(
                query = state.query,
                onQueryChange = { component.changeQuery(it) },
                onSearch = { component.onClickSearch() },
                active = state.query.isNotEmpty(),
                onActiveChange = {},
                leadingIcon = {
                    IconButton(
                        onClick = { component.onClickBack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            tint = MaterialTheme.colorScheme.onBackground,
                            contentDescription = "",
                        )
                    }
                },
                trailingIcon = {
                    IconButton(
                        onClick = { component.onClickSearch() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            tint = MaterialTheme.colorScheme.onBackground,
                            contentDescription = "",
                        )
                    }
                }
            ) {
                when (val searchstate = state.searchState) {
                    is SearchStore.State.SearchState.Empty -> {
                        Text(
                            text = "Nothing to show"
                        )
                    }
                    is SearchStore.State.SearchState.Error -> {
                        ErrorScreen()
                    }
                    is SearchStore.State.SearchState.Initial -> {
                        InitialScreen()
                    }
                    is SearchStore.State.SearchState.Loading -> {
                        FullScreenProgress()
                    }
                    is SearchStore.State.SearchState.Success -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            items(
                                items = searchstate.cities,
                                key = { it.name }
                            ) {
                                Text(
                                    modifier = Modifier.clickable { component.onClickCity(it) },
                                    text = it.name
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
