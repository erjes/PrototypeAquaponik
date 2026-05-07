package com.cibiruwetan.protoaquaponik.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.cibiruwetan.protoaquaponik.ui.components.PpmGauge
import com.cibiruwetan.protoaquaponik.ui.components.StatusBox
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

    val database = remember(kolamId) {
        Firebase.database.getReference("kolam/$kolamId")
    }
    val tdsValue = remember { mutableIntStateOf(0) }
    val isBuzzerActive = remember { mutableStateOf(false) }
    val isPumpActive = remember { mutableStateOf(false) }
    val locationValue = remember { mutableStateOf("") }

        DisposableEffect(kolamId) {
        val listener = database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tdsValue.intValue = snapshot.child("tds_simulasi").getValue(Int::class.java) ?: 0
                isBuzzerActive.value = snapshot.child("status_buzzer").getValue(Boolean::class.java) ?: false
                isPumpActive.value = snapshot.child("status_pompa").getValue(Boolean::class.java) ?: false
                locationValue.value = snapshot.child("lokasi").getValue(String::class.java) ?: ""

            }
            override fun onCancelled(error: DatabaseError) {}
        })
        onDispose { database.removeEventListener(listener) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = ToscaPrimary,
            shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = stringResource(R.string.label_kolam_terpilih),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Text(
                    text = kolamId,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.label_lokasi, locationValue.value),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.9f),
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Gauge Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Box(
                    modifier = Modifier.padding(24.dp).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    PpmGauge(value = tdsValue.intValue)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatusBox(
                    label = stringResource(R.string.status_air),
                    value = if (tdsValue.intValue < 10) "Keruh" else "Stabil",
                    valueColor = if (tdsValue.intValue < 10) StatusRed else StatusGreen
                )
                StatusBox(
                    label = stringResource(R.string.status_tds),
                    value = if (tdsValue.intValue < 10) "Tinggi" else "Normal",
                    valueColor = if (tdsValue.intValue < 10) Color.Yellow else StatusGreen
                )
                StatusBox(
                    label = stringResource(R.string.status_pompa),
                    value = if (isPumpActive.value) stringResource(R.string.status_aktif) else stringResource(R.string.status_nonaktif),
                    valueColor = if (isPumpActive.value) StatusGreen else Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = stringResource(R.string.status_buzzer), style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = if (isBuzzerActive.value) stringResource(R.string.status_aktif) else stringResource(R.string.status_nonaktif),
                            color = if (!isBuzzerActive.value) StatusRed else StatusGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha = 0.3f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = stringResource(R.string.status_pompa), style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = if (isPumpActive.value) stringResource(R.string.status_aktif) else stringResource(R.string.status_nonaktif),
                            color = if (!isPumpActive.value) StatusRed else StatusGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha = 0.3f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = stringResource(R.string.label_ppm), style = MaterialTheme.typography.bodyMedium)
                        Text(text = "10 - 50", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}