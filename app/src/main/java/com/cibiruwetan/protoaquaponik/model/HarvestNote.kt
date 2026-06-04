package com.cibiruwetan.protoaquaponik.model

data class HarvestNote(
    val id: String = "",
    val kolamId: String = "",
    val jenisPanen: String = "",
    val namaPanen: String = "",
    val beratKg: Double = 0.0,
    val tanggalPanen: Long = 0L,
    val deskripsi: String = "",
    val createdAt: Long = 0L
)