package com.ivos.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.defaultComponentContext
import com.ivos.domain.usecases.favorites.ChangeCityIsFavoriteStatusUseCase
import com.ivos.domain.usecases.search.SearchCityUseCase
import com.ivos.presentation.root.RootComponentImpl
import com.ivos.presentation.root.RootContent
import com.ivos.presentation.ui.theme.IvosWeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var rootFactory: RootComponentImpl.Factory

    @Inject lateinit var search: SearchCityUseCase
    @Inject lateinit var fav: ChangeCityIsFavoriteStatusUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        enableEdgeToEdge()
        setContent {
            IvosWeatherAppTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding(),
                ) {
                    Scaffold(
                        containerColor = MaterialTheme.colorScheme.background,
                        topBar = {
                            Box(modifier = Modifier.systemBarsPadding())
                        },
                        floatingActionButton = {
                            //todo remove
                            /*FloatingActionButton(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape),
                                onClick = {
                                    CoroutineScope(Dispatchers.Main).launch {
                                        search.invoke("понa").forEach {
                                            fav.invoke(it, true)
                                        }
                                    }
                                }
                            ) {
                                Text(
                                    text = "+",
                                    fontSize = 48.sp
                                )
                            }*/
                        }
                    ) {
                        RootContent(
                            paddingValues = it,
                            component = rootFactory.create(defaultComponentContext()),
                        )
                    }
                }
            }
        }
    }
}
