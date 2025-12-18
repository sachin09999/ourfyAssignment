package com.sachin.assignmnet.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object WebView : Screen("webview/{url}") {
        fun createRoute(url: String): String = "webview/$url"
    }
    object History : Screen("history")
}
