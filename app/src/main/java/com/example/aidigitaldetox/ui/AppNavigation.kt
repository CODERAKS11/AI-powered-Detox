package com.example.aidigitaldetox.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "dashboard") {
        composable("dashboard") {
            DashboardScreen()
        }
        composable("lock_screen/{packageName}") { backStackEntry ->
            val packageName = backStackEntry.arguments?.getString("packageName") ?: ""
            LockScreen(
                packageName = packageName,
                viewModel = hiltViewModel(),
                onUnlock = { navController.popBackStack() },
                onExit = { navController.popBackStack() }
            )
        }
    }
}
