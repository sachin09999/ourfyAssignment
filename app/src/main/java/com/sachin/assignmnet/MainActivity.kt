package com.sachin.assignmnet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.sachin.assignmnet.ui.navigation.AppNavHost
import com.sachin.assignmnet.ui.theme.AssignmentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AssignmentTheme {
                val navController = rememberNavController()
                val app = application as OurfyApplication
                AppNavHost(navController = navController, repository = app.repository)
            }
        }
    }
}