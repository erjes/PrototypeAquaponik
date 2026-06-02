package com.cibiruwetan.protoaquaponik.model

data class FishPanelRecord(
    val id: String = "",
    val jumlahIkan: Int = 0,
    val pakanGram: Int = 0,
    val mortalitas: Int = 0,
    val catatan: String = "",
    val createdAt: Long = 0L
)
