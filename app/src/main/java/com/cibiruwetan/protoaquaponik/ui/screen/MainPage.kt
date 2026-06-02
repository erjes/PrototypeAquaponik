package com.cibiruwetan.protoaquaponik.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cibiruwetan.protoaquaponik.R
import com.cibiruwetan.protoaquaponik.ui.components.BaseTopAppBar
import com.cibiruwetan.protoaquaponik.ui.components.BottomNavigationBar
import com.cibiruwetan.protoaquaponik.ui.navigation.AppNavigation
import com.cibiruwetan.protoaquaponik.ui.navigation.Screen
import com.cibiruwetan.protoaquaponik.ui.viewmodel.SharedViewModel

@Composable
fun MainPage(sharedViewModel: SharedViewModel = viewModel()) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val title = when {
        currentRoute == Screen.KolamOverview.route -> stringResource(R.string.title_beranda)
        currentRoute == Screen.Riwayat.route || currentRoute?.startsWith("riwayat") == true -> stringResource(R.string.title_riwayat)
        currentRoute == Screen.Warning.route -> stringResource(R.string.title_warning)
        currentRoute?.startsWith("sensor") == true -> stringResource(R.string.title_sensor)
        currentRoute?.startsWith("realtime") == true -> stringResource(R.string.realtime_title)
        else -> stringResource(R.string.app_name)
    }

    val canNavigateBack = navController.previousBackStackEntry != null

    val isMonitoringPage = currentRoute?.startsWith("sensor") == true ||
            currentRoute == Screen.Riwayat.route ||
            currentRoute == Screen.Warning.route

    val showBottomBar = currentRoute == Screen.KolamOverview.route || isMonitoringPage
    val showDropdown = isMonitoringPage && sharedViewModel.daftarKolam.isNotEmpty()
    val selectedKolamLabel = when {
        sharedViewModel.isLoadingKolam.value -> stringResource(R.string.content_description_loading)
        sharedViewModel.hasDatabaseError.value -> stringResource(R.string.state_database_error)
        sharedViewModel.daftarKolam.isEmpty() -> stringResource(R.string.state_no_pond)
        else -> sharedViewModel.selectedKolamId.value
    }

    Scaffold(
        topBar = {
            BaseTopAppBar(
                title = title,
                showBackButton = canNavigateBack,
                showDropdown = showDropdown,
                selectedId = selectedKolamLabel,
                listOptions = sharedViewModel.daftarKolam,
                onOptionSelected = { sharedViewModel.updateSelectedKolam(it) },
                onBackClick = { navController.navigateUp() }
            )
        },
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController, sharedViewModel)
            }
        }
    ) { innerPadding ->
        AppNavigation(
            navController = navController,
            sharedViewModel = sharedViewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
