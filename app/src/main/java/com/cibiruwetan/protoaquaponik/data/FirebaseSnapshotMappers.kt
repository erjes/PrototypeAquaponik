package com.cibiruwetan.protoaquaponik.data

import com.cibiruwetan.protoaquaponik.model.HistoryPoint
import com.cibiruwetan.protoaquaponik.model.FishPanelRecord
import com.cibiruwetan.protoaquaponik.model.Kolam
import com.cibiruwetan.protoaquaponik.model.KolamTelemetry
import com.cibiruwetan.protoaquaponik.model.PlantHarvestRecord
import com.cibiruwetan.protoaquaponik.model.WarningLog
import com.google.firebase.database.DataSnapshot
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val TDS_MIN_NORMAL = 10
const val TDS_MAX_NORMAL = 1000

fun Int.isTdsNormal(): Boolean = this in TDS_MIN_NORMAL..TDS_MAX_NORMAL

fun Int.isTdsAnomaly(): Boolean = !isTdsNormal()

fun DataSnapshot.toKolamModel(): Kolam {
    return Kolam(
        id = key.orEmpty(),
        nama = child("nama").stringValue().orEmpty(),
        lokasi = child("lokasi").stringValue().orEmpty(),
        status_buzzer = child("status_buzzer").strictBooleanValue() ?: false,
        status_pompa = child("status_pompa").strictBooleanValue() ?: false,
        tds_simulasi = child("tds_simulasi").intValue() ?: 0,
        isAktif = child("isAktif").strictBooleanValue() ?: true,
        statusAir = child("statusAir").stringValue().orEmpty()
    )
}

fun DataSnapshot.toKolamTelemetry(): KolamTelemetry {
    return KolamTelemetry(
        tdsSimulasi = child("tds_simulasi").intValue() ?: 0,
        statusBuzzer = child("status_buzzer").strictBooleanValue() ?: false,
        statusPompa = child("status_pompa").strictBooleanValue() ?: false,
        lokasi = child("lokasi").stringValue().orEmpty()
    )
}

fun DataSnapshot.toHistoryPoints(nowMillis: Long = System.currentTimeMillis()): List<HistoryPoint> {
    val riwayatNode = child("riwayat")
    val points = if (riwayatNode.childrenCount > 0) {
        riwayatNode.children.mapIndexedNotNull { index, item ->
            item.toHistoryPoint(index)
        }
    } else {
        listOfNotNull(toCurrentHistoryPoint(nowMillis))
    }

    val cutoffMillis = nowMillis - LAST_24_HOURS_MILLIS
    val pointsFromLastDay = points.filter { it.timestamp >= cutoffMillis && it.timestamp > MIN_REAL_TIMESTAMP }
    return (pointsFromLastDay.ifEmpty { points })
        .sortedWith(compareBy<HistoryPoint> { it.timestamp }.thenBy { it.id })
        .takeLast(MAX_CHART_POINTS)
}

fun DataSnapshot.toTdsWarningLogs(
    kolamId: String,
    nowMillis: Long = System.currentTimeMillis()
): List<WarningLog> {
    return toHistoryPoints(nowMillis)
        .filter { it.ppm.isTdsAnomaly() }
        .map { point ->
            WarningLog(
                id = "${kolamId}_${point.id}_${point.timestamp}",
                kolamId = kolamId,
                ppm = point.ppm,
                timestamp = point.timestamp,
                timeLabel = point.toWarningTimeLabel()
            )
        }
        .sortedByDescending { it.timestamp }
}

fun DataSnapshot.toFishPanelRecord(): FishPanelRecord {
    return FishPanelRecord(
        id = key.orEmpty(),
        jumlahIkan = child("jumlahIkan").intValue() ?: 0,
        pakanGram = child("pakanGram").intValue() ?: 0,
        mortalitas = child("mortalitas").intValue() ?: 0,
        catatan = child("catatan").stringValue().orEmpty(),
        createdAt = child("createdAt").longValue() ?: 0L
    )
}

fun DataSnapshot.toPlantHarvestRecord(): PlantHarvestRecord {
    return PlantHarvestRecord(
        id = key.orEmpty(),
        namaTanaman = child("namaTanaman").stringValue().orEmpty(),
        beratKg = child("beratKg").doubleValue() ?: 0.0,
        jumlahIkat = child("jumlahIkat").intValue() ?: 0,
        catatan = child("catatan").stringValue().orEmpty(),
        createdAt = child("createdAt").longValue() ?: 0L
    )
}

private fun DataSnapshot.toHistoryPoint(fallbackIndex: Int): HistoryPoint? {
    val ppm = child("ppm").intValue()
        ?: child("tds_simulasi").intValue()
        ?: intValue()
        ?: return null

    val timestamp = child("timestamp").longValue()
        ?: child("createdAt").longValue()
        ?: child("waktuMillis").longValue()
        ?: fallbackIndex.toLong()

    val label = child("tanggal").stringValue()
        ?: child("waktu").stringValue()
        ?: child("time").stringValue()
        ?: child("jam").stringValue()
        ?: key.orEmpty()

    return HistoryPoint(
        id = key ?: fallbackIndex.toString(),
        label = label,
        ppm = ppm,
        timestamp = timestamp
    )
}

private fun DataSnapshot.toCurrentHistoryPoint(nowMillis: Long): HistoryPoint? {
    val ppm = child("tds_simulasi").intValue() ?: return null
    return HistoryPoint(
        id = key.orEmpty(),
        label = key.orEmpty(),
        ppm = ppm,
        timestamp = nowMillis
    )
}

private fun HistoryPoint.toWarningTimeLabel(): String {
    if (label.isNotBlank()) return label
    if (timestamp <= MIN_REAL_TIMESTAMP) return ""
    return SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(timestamp))
}

private fun DataSnapshot.stringValue(): String? = value as? String

private fun DataSnapshot.strictBooleanValue(): Boolean? = value as? Boolean

private fun DataSnapshot.intValue(): Int? {
    return when (val rawValue = value) {
        is Int -> rawValue
        is Long -> rawValue.toInt()
        is Double -> rawValue.toInt()
        is Float -> rawValue.toInt()
        is String -> rawValue.toIntOrNull()
        else -> null
    }
}

private fun DataSnapshot.longValue(): Long? {
    return when (val rawValue = value) {
        is Long -> rawValue
        is Int -> rawValue.toLong()
        is Double -> rawValue.toLong()
        is Float -> rawValue.toLong()
        is String -> rawValue.toLongOrNull()
        else -> null
    }
}

private fun DataSnapshot.doubleValue(): Double? {
    return when (val rawValue = value) {
        is Double -> rawValue
        is Float -> rawValue.toDouble()
        is Long -> rawValue.toDouble()
        is Int -> rawValue.toDouble()
        is String -> rawValue.replace(',', '.').toDoubleOrNull()
        else -> null
    }
}

private const val LAST_24_HOURS_MILLIS = 24L * 60L * 60L * 1000L
private const val MIN_REAL_TIMESTAMP = 1_000_000_000_000L
private const val MAX_CHART_POINTS = 24
