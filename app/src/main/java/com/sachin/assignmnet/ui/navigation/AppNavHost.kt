package com.sachin.assignmnet.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sachin.assignmnet.data.repository.HistoryRepository
import com.sachin.assignmnet.ui.screens.home.HomeScreen
import com.sachin.assignmnet.ui.screens.home.HomeViewModel
import com.sachin.assignmnet.ui.screens.home.HomeViewModelFactory
import com.sachin.assignmnet.ui.screens.webview.WebViewScreen


import com.sachin.assignmnet.ui.screens.history.HistoryScreen
import com.sachin.assignmnet.ui.screens.history.HistoryViewModel
import com.sachin.assignmnet.ui.screens.history.HistoryViewModelFactory

@Composable
fun AppNavHost(
    navController: NavHostController,
    repository: HistoryRepository
) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { backStackEntry ->
            val viewModel: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(repository)
            )

            // Handle "clear_input" result from WebView
            val savedStateHandle = backStackEntry.savedStateHandle
            val clearInput by savedStateHandle.getStateFlow("clear_input", false).collectAsState()

            LaunchedEffect(clearInput) {
                if (clearInput) {
                    viewModel.onClearInput()
                    savedStateHandle["clear_input"] = false
                }
            }

            HomeScreen(
                viewModel = viewModel,
                onNavigateToWebView = { url ->
                    navController.navigate(Screen.WebView.createRoute(url))
                },
                onNavigateToHistory = {
                    navController.navigate(Screen.History.route)
                }
            )
        }
        composable(Screen.WebView.route) { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url") ?: ""
            WebViewScreen(
                url = url,
                onBack = {
                    navController.popBackStack()
                },
                onClose = {
                    navController.previousBackStackEntry?.savedStateHandle?.set("clear_input", true)
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.History.route) {
            val viewModel: HistoryViewModel = viewModel(
                factory = HistoryViewModelFactory(repository)
            )
            HistoryScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

