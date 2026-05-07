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
import com.cibiruwetan.protoaquaponik.ui.viewmodel.SharedViewModel

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val activeKolamId = sharedViewModel.selectedKolamId.value

    val items = listOf(
        NavigationItem(stringResource(R.string.nav_beranda), Icons.Default.Home, Screen.KolamOverview.route),
        NavigationItem(stringResource(R.string.nav_sensor), Icons.Default.Waves, "sensor/$activeKolamId"),
        NavigationItem(stringResource(R.string.nav_riwayat), Icons.Default.Speed, "riwayat/$activeKolamId"),
        NavigationItem(stringResource(R.string.nav_warning), Icons.Default.History, Screen.Warning.route)
    )

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val isSelected = when {
                item.route.startsWith("sensor") -> currentRoute?.startsWith("sensor") == true
                else -> currentRoute == item.route
            }

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
                    if (item.route.contains("{kolamId}")) {
                        val actualRoute = item.route.replace("{kolamId}", sharedViewModel.selectedKolamId.value)
                        navController.navigate(actualRoute)
                    } else {
                        navController.navigate(item.route)
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