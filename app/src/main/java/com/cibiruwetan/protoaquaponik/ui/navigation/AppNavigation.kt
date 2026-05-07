package com.cibiruwetan.protoaquaponik.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cibiruwetan.protoaquaponik.ui.screen.KolamOverviewPage
import com.cibiruwetan.protoaquaponik.ui.screen.RealtimePage
import com.cibiruwetan.protoaquaponik.ui.screen.RiwayatPage
import com.cibiruwetan.protoaquaponik.ui.screen.SensorPage
import com.cibiruwetan.protoaquaponik.ui.screen.WarningPage
import com.cibiruwetan.protoaquaponik.ui.viewmodel.SharedViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.KolamOverview.route,
        modifier = modifier
    ) {
        composable(Screen.KolamOverview.route) {
            KolamOverviewPage(
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }

        composable(
            route = Screen.Realtime.route,
            arguments = listOf(navArgument("kolamId") { type = NavType.StringType })
        ) { backStackEntry ->
            val idFromArgs = backStackEntry.arguments?.getString("kolamId")

            LaunchedEffect(idFromArgs) {
                idFromArgs?.let { sharedViewModel.updateSelectedKolam(it) }
            }

            RealtimePage(sharedViewModel = sharedViewModel)
        }

        composable(
            route = Screen.Sensor.route,
            arguments = listOf(navArgument("kolamId") { type = NavType.StringType })
        ) { backStackEntry ->
            val idFromArgs = backStackEntry.arguments?.getString("kolamId")

            LaunchedEffect(idFromArgs) {
                idFromArgs?.let { sharedViewModel.updateSelectedKolam(it) }
            }

            SensorPage(
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }

        composable(Screen.Riwayat.route) {
            RiwayatPage(navController = navController, sharedViewModel = sharedViewModel)
        }

        composable(Screen.Warning.route) {
            WarningPage(navController = navController, sharedViewModel = sharedViewModel)
        }
    }
}