package com.cibiruwetan.protoaquaponik.model


data class Kolam(
    val id: String = "",
    val nama: String = "",
    val lokasi: String = "",
    val status_buzzer: Boolean = false,
    val status_pompa: Boolean = false,
    val tds_simulasi: Int = 0,
    val isAktif: Boolean = true,
    val statusAir: String = ""
)