package com.cibiruwetan.protoaquaponik.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.HeatPump
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cibiruwetan.protoaquaponik.R
import com.cibiruwetan.protoaquaponik.data.isTdsNormal
import com.cibiruwetan.protoaquaponik.data.toKolamTelemetry
import com.cibiruwetan.protoaquaponik.model.KolamTelemetry
import com.cibiruwetan.protoaquaponik.model.Sensor
import com.cibiruwetan.protoaquaponik.ui.components.SensorItemCard
import com.cibiruwetan.protoaquaponik.ui.theme.BackgroundLight
import com.cibiruwetan.protoaquaponik.ui.theme.ToscaPrimary
import com.cibiruwetan.protoaquaponik.ui.viewmodel.SharedViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SensorPage(
    navController: NavController,
    sharedViewModel: SharedViewModel
) {
    val kolamId by sharedViewModel.selectedKolamId
    var telemetry by remember { mutableStateOf(KolamTelemetry()) }
    var lastUpdate by remember { mutableStateOf("-") }

    val txtAktif = stringResource(R.string.status_aktif)
    val txtNonaktif = stringResource(R.string.status_nonaktif)
    val locationLabel = telemetry.lokasi.ifBlank { stringResource(R.string.label_location_unknown) }
    val tdsIsNormal = telemetry.tdsSimulasi.isTdsNormal()

    DisposableEffect(kolamId) {
        if (kolamId.isBlank()) {
            telemetry = KolamTelemetry()
            onDispose {}
        } else {
            val database = Firebase.database.getReference("kolam/$kolamId")
            val listener = database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                    telemetry = snapshot.toKolamTelemetry()

                val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                lastUpdate = sdf.format(Date())
            }

            override fun onCancelled(error: DatabaseError) {}
        })
            onDispose { database.removeEventListener(listener) }
        }
    }

    val sensorList = listOf(
        Sensor(
            name = stringResource(R.string.status_tds),
            value = telemetry.tdsSimulasi.toString(),
            unit = stringResource(R.string.label_ppm),
            icon = Icons.Default.Waves,
            isNormal = tdsIsNormal
        ),
        Sensor(
            name = stringResource(R.string.status_buzzer),
            value = if (telemetry.statusBuzzer) txtAktif else txtNonaktif,
            unit = "",
            icon = Icons.Default.Alarm,
            isNormal = !telemetry.statusBuzzer
        ),
        Sensor(
            name = stringResource(R.string.status_pompa),
            value = if (telemetry.statusPompa) txtAktif else txtNonaktif,
            unit = "",
            icon = Icons.Default.HeatPump,
            isNormal = telemetry.statusPompa
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = ToscaPrimary,
            shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.last_update, lastUpdate),
                    color = Color.White.copy(alpha = 0.9f),
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = stringResource(R.string.label_lokasi, locationLabel),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.9f),
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = stringResource(R.string.label_list_sensor),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            items(sensorList) { sensor ->
                SensorItemCard(sensor)
            }
        }
    }
}
