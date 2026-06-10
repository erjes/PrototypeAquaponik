package com.cibiruwetan.protoaquaponik.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.HeatPump
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cibiruwetan.protoaquaponik.R
import com.cibiruwetan.protoaquaponik.data.isTdsNormal
import com.cibiruwetan.protoaquaponik.data.toKolamTelemetry
import com.cibiruwetan.protoaquaponik.model.KolamTelemetry
import com.cibiruwetan.protoaquaponik.ui.components.PpmGauge
import com.cibiruwetan.protoaquaponik.ui.components.SensorMetricCard
import com.cibiruwetan.protoaquaponik.ui.components.StatusBadge
import com.cibiruwetan.protoaquaponik.ui.theme.*
import com.cibiruwetan.protoaquaponik.ui.viewmodel.SharedViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

@Composable
fun RealtimePage(
    sharedViewModel: SharedViewModel
) {
    val kolamId by sharedViewModel.selectedKolamId
    var telemetry by remember { mutableStateOf(KolamTelemetry()) }

    val tdsIsNormal = telemetry.tdsSimulasi.isTdsNormal()
    val kolamLabel = kolamId.ifBlank { stringResource(R.string.content_description_loading) }
    val locationLabel = telemetry.lokasi.ifBlank { stringResource(R.string.label_location_unknown) }
    val activeText = stringResource(R.string.status_aktif)
    val inactiveText = stringResource(R.string.status_nonaktif)

    DisposableEffect(kolamId) {
        if (kolamId.isBlank()) {
            telemetry = KolamTelemetry()
            onDispose {}
        } else {
            val database = Firebase.database.getReference("kolam/$kolamId")
            val listener = database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                    telemetry = snapshot.toKolamTelemetry()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
            onDispose { database.removeEventListener(listener) }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = ToscaPrimary,
            shape = RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp),
            shadowElevation = 6.dp
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = stringResource(R.string.label_kolam_terpilih),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Text(
                    text = kolamLabel,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.label_lokasi, locationLabel),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.9f),
                )
                Spacer(modifier = Modifier.height(12.dp))
                StatusBadge(isNormal = tdsIsNormal)
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .padding(28.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        PpmGauge(value = telemetry.tdsSimulasi)
                    }
                }
            }

            item {
                SensorMetricCard(
                    label = stringResource(R.string.status_tds),
                    value = telemetry.tdsSimulasi.toString(),
                    unit = stringResource(R.string.label_ppm),
                    icon = Icons.Default.Waves,
                    isNormal = tdsIsNormal
                )
            }
            item {
                SensorMetricCard(
                    label = stringResource(R.string.status_pompa),
                    value = if (telemetry.statusPompa) activeText else inactiveText,
                    unit = "",
                    icon = Icons.Default.HeatPump,
                    isNormal = telemetry.statusPompa
                )
            }
            item {
                SensorMetricCard(
                    label = stringResource(R.string.status_buzzer),
                    value = if (telemetry.statusBuzzer) activeText else inactiveText,
                    unit = "",
                    icon = Icons.Default.Alarm,
                    isNormal = !telemetry.statusBuzzer
                )
            }
        }
    }
}
