package com.cibiruwetan.protoaquaponik.model

data class Riwayat(
    val id: String = "",
    val tanggal: String = "",
    val ppm: Int = 0,
    val statusAir: String = "",
    val statusTds: String = "",
    val statusPompa: String = "",
    val statusBuzzer: String = "",
    val deskripsi: String = ""
)