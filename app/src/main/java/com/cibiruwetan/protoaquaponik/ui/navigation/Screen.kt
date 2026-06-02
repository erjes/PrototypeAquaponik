package com.cibiruwetan.protoaquaponik.ui.navigation

sealed class Screen(val route: String) {
    object KolamOverview : Screen("kolam_overview")
    object Realtime : Screen("realtime/{kolamId}")
    object Sensor : Screen("sensor/{kolamId}")
    object Riwayat : Screen("riwayat/{kolamId}")
    object Warning : Screen("peringatan")
    object FishPanel : Screen("ikan/{kolamId}")
    object PlantHarvest : Screen("panen/{kolamId}")
}

