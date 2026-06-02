package com.cibiruwetan.protoaquaponik.model

data class HistoryPoint(
    val id: String = "",
    val label: String = "",
    val ppm: Int = 0,
    val timestamp: Long = 0L
)
