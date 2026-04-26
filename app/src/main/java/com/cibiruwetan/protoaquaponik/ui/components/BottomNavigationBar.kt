package com.cibiruwetan.protoaquaponik.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.cibiruwetan.protoaquaponik.R
import com.cibiruwetan.protoaquaponik.ui.navigation.Screen
import com.cibiruwetan.protoaquaponik.ui.theme.ToscaPrimary

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val items = listOf(
        NavigationItem(stringResource(id = R.string.nav_beranda), Icons.Default.Home, Screen.KolamOverview.route),
        NavigationItem(stringResource(id = R.string.nav_sensor), Icons.Default.Waves, Screen.Sensor.route),
        NavigationItem(stringResource(id = R.string.nav_riwayat), Icons.Default.Speed, Screen.Riwayat.route),
        NavigationItem(stringResource(id = R.string.nav_warning), Icons.Default.History, Screen.Warning.route)
    )

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = if (isSelected) ToscaPrimary else Color.Gray
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        color = if (isSelected) ToscaPrimary else Color.Gray,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                selected = isSelected,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = ToscaPrimary.copy(alpha = 0.1f)
                )
            )
        }
    }
}

data class NavigationItem(val title: String, val icon: ImageVector, val route: String)