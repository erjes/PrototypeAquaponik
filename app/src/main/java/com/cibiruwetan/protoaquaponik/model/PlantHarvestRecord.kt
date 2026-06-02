package com.cibiruwetan.protoaquaponik.model

data class PlantHarvestRecord(
    val id: String = "",
    val namaTanaman: String = "",
    val beratKg: Double = 0.0,
    val jumlahIkat: Int = 0,
    val catatan: String = "",
    val createdAt: Long = 0L
)
