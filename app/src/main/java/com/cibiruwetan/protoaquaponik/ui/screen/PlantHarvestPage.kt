package com.cibiruwetan.protoaquaponik.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.cibiruwetan.protoaquaponik.ui.theme.ToscaPrimary
import com.cibiruwetan.protoaquaponik.ui.viewmodel.PlantHarvestViewModel
import com.cibiruwetan.protoaquaponik.ui.viewmodel.SharedViewModel
import java.text.SimpleDateFormat
import java.util.*

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
        contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 16.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
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
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.harvest_form_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A2E)
                    )

                    OutlinedTextField(
                        value = namaTanaman,
                        onValueChange = { namaTanaman = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(R.string.harvest_crop_label)) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = beratKg,
                        onValueChange = { input ->
                            beratKg = input.filter { it.isDigit() || it == '.' || it == ',' }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(R.string.harvest_weight_label)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = jumlahIkat,
                        onValueChange = { jumlahIkat = it.filter(Char::isDigit) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(R.string.harvest_bundle_label)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = catatan,
                        onValueChange = { catatan = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(R.string.label_notes)) },
                        minLines = 2,
                        shape = RoundedCornerShape(12.dp)
                    )

                    if (errorTextRes != 0) {
                        Text(
                            text = stringResource(errorTextRes),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    Button(
                        onClick = {
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
                                namaTanaman = ""; beratKg = ""; jumlahIkat = ""; catatan = ""; errorTextRes = 0
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ToscaPrimary)
                    ) {
                        Text(
                            stringResource(R.string.action_save_harvest),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = stringResource(R.string.harvest_history_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A2E),
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
private fun PlantHarvestRecordCard(record: PlantHarvestRecord) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = formatHarvestTime(record.createdAt),
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = record.namaTanaman,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A2E)
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = ToscaPrimary.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = stringResource(R.string.harvest_weight_value, record.beratKg),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelLarge,
                        color = ToscaPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Text(
                text = stringResource(R.string.harvest_bundle_unit_value, record.jumlahIkat),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            if (record.catatan.isNotBlank()) {
                HorizontalDivider(color = Color(0xFFF0F0F0))
                Text(
                    text = record.catatan,
                    style = MaterialTheme.typography.bodySmall,
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