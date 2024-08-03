package io.github.hanihashemi.eggmaster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.hanihashemi.eggmaster.ui.theme.EggMasterTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewEvent()
        setContent {
            val state by viewModel.viewState.collectAsState()
            navController = rememberNavController()
            EggMasterTheme {
                EggMasterApp(navController, viewModel, state)
            }
        }
    }

    private fun observeViewEvent() {
        lifecycleScope.launch {
            viewModel.viewEvent.collectLatest { viewEvent ->
                when (viewEvent) {
                    is MainViewModel.ViewEvent.OpenNextPage -> {
                        // Handle open next page event
                    }
                    is MainViewModel.ViewEvent.NavigateBack -> {
                        onBackPressedDispatcher.onBackPressed()
                    }
                }
            }
        }
    }
}