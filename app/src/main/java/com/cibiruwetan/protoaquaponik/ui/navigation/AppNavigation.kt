package com.cibiruwetan.protoaquaponik.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cibiruwetan.protoaquaponik.ui.screen.KolamOverviewPage
import com.cibiruwetan.protoaquaponik.ui.screen.RealtimePage

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.KolamOverview.route,
        modifier = modifier
    ) {
        composable(Screen.KolamOverview.route) {
            KolamOverviewPage(navController = navController)
        }

        composable(
            route = Screen.Realtime.route,
            arguments = listOf(navArgument("kolamId") { type = NavType.StringType })
        ) { backStackEntry ->
            val kolamId = backStackEntry.arguments?.getString("kolamId") ?: "Unknown"
            RealtimePage(kolamId = kolamId)
        }

        composable(Screen.Riwayat.route) {
        }
    }
}