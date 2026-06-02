package com.cibiruwetan.protoaquaponik.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cibiruwetan.protoaquaponik.R
import com.cibiruwetan.protoaquaponik.model.PlantHarvestRecord
import com.cibiruwetan.protoaquaponik.ui.components.SensorMetricCard
import com.cibiruwetan.protoaquaponik.ui.theme.BackgroundLight
import com.cibiruwetan.protoaquaponik.ui.viewmodel.PlantHarvestViewModel
import com.cibiruwetan.protoaquaponik.ui.viewmodel.SharedViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PlantHarvestPage(
    sharedViewModel: SharedViewModel,
    viewModel: PlantHarvestViewModel = viewModel()
) {
    val kolamId by sharedViewModel.selectedKolamId
    val records by viewModel.records.collectAsState()

    var namaTanaman by remember { mutableStateOf("") }
    var beratKg by remember { mutableStateOf("") }
    var jumlahIkat by remember { mutableStateOf("") }
    var catatan by remember { mutableStateOf("") }
    var errorTextRes by remember { mutableIntStateOf(0) }

    val totalBerat = records.sumOf { it.beratKg }
    val totalIkat = records.sumOf { it.jumlahIkat }

    DisposableEffect(kolamId) {
        viewModel.observeKolam(kolamId)
        onDispose { viewModel.stopObserving() }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            SensorMetricCard(
                label = stringResource(R.string.harvest_total_record),
                value = stringResource(R.string.harvest_weight_value, totalBerat),
                unit = stringResource(R.string.harvest_bundle_unit_value, totalIkat),
                icon = Icons.Default.CheckCircle,
                isNormal = records.isNotEmpty()
            )
        }

        item {
            PlantHarvestForm(
                namaTanaman = namaTanaman,
                beratKg = beratKg,
                jumlahIkat = jumlahIkat,
                catatan = catatan,
                errorTextRes = errorTextRes,
                onNamaTanamanChange = { namaTanaman = it },
                onBeratKgChange = { input ->
                    beratKg = input.filter { it.isDigit() || it == '.' || it == ',' }
                },
                onJumlahIkatChange = { jumlahIkat = it.filter(Char::isDigit) },
                onCatatanChange = { catatan = it },
                onSaveClick = {
                    val berat = beratKg.replace(',', '.').toDoubleOrNull()
                    val ikat = jumlahIkat.toIntOrNull() ?: 0
                    val tanaman = namaTanaman.trim()

                    if (tanaman.isBlank() || berat == null) {
                        errorTextRes = R.string.form_error_harvest_required
                    } else {
                        viewModel.addRecord(
                            kolamId = kolamId,
                            namaTanaman = tanaman,
                            beratKg = berat,
                            jumlahIkat = ikat,
                            catatan = catatan.trim()
                        )
                        namaTanaman = ""
                        beratKg = ""
                        jumlahIkat = ""
                        catatan = ""
                        errorTextRes = 0
                    }
                }
            )
        }

        item {
            Text(
                text = stringResource(R.string.harvest_history_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        if (records.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.harvest_empty),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 24.dp)
                )
            }
        } else {
            items(records, key = { it.id }) { record ->
                PlantHarvestRecordCard(record)
            }
        }
    }
}

@Composable
private fun PlantHarvestForm(
    namaTanaman: String,
    beratKg: String,
    jumlahIkat: String,
    catatan: String,
    errorTextRes: Int,
    onNamaTanamanChange: (String) -> Unit,
    onBeratKgChange: (String) -> Unit,
    onJumlahIkatChange: (String) -> Unit,
    onCatatanChange: (String) -> Unit,
    onSaveClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = stringResource(R.string.harvest_form_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = namaTanaman,
                onValueChange = onNamaTanamanChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.harvest_crop_label)) },
                singleLine = true
            )

            OutlinedTextField(
                value = beratKg,
                onValueChange = onBeratKgChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.harvest_weight_label)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )

            OutlinedTextField(
                value = jumlahIkat,
                onValueChange = onJumlahIkatChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.harvest_bundle_label)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            OutlinedTextField(
                value = catatan,
                onValueChange = onCatatanChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.label_notes)) },
                minLines = 2
            )

            if (errorTextRes != 0) {
                Text(
                    text = stringResource(errorTextRes),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                onClick = onSaveClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.action_save_harvest))
            }
        }
    }
}

@Composable
private fun PlantHarvestRecordCard(record: PlantHarvestRecord) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = formatHarvestTime(record.createdAt),
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = record.namaTanaman,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.harvest_weight_value, record.beratKg),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Text(
                text = stringResource(R.string.harvest_bundle_unit_value, record.jumlahIkat),
                style = MaterialTheme.typography.bodyMedium
            )
            if (record.catatan.isNotBlank()) {
                Text(
                    text = record.catatan,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}

private fun formatHarvestTime(timestamp: Long): String {
    if (timestamp <= 0L) return "-"
    return SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date(timestamp))
}
