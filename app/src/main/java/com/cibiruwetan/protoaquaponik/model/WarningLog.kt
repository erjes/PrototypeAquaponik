package com.cibiruwetan.protoaquaponik.model

data class WarningLog(
    val id: String = "",
    val kolamId: String = "",
    val ppm: Int = 0,
    val timestamp: Long = 0L,
    val timeLabel: String = ""
)
