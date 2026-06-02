package com.cibiruwetan.protoaquaponik.model

data class KolamTelemetry(
    val tdsSimulasi: Int = 0,
    val statusBuzzer: Boolean = false,
    val statusPompa: Boolean = false,
    val lokasi: String = ""
)
