package com.cibiruwetan.protoaquaponik.model

data class FishPanelRecord(
    val id: String = "",
    val jenisIkan: String = "", //jmlh
    val tanggalPanen: Int = 0, //pakangr
    val totalPanen: Int = 0, //morta
    val catatan: String = "",
    val createdAt: Long = 0L
)
