package com.cibiruwetan.protoaquaponik.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WaterDrop
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
import com.cibiruwetan.protoaquaponik.model.FishPanelRecord
import com.cibiruwetan.protoaquaponik.ui.components.SensorMetricCard
import com.cibiruwetan.protoaquaponik.ui.theme.BackgroundLight
import com.cibiruwetan.protoaquaponik.ui.theme.ToscaPrimary
import com.cibiruwetan.protoaquaponik.ui.viewmodel.FishPanelViewModel
import com.cibiruwetan.protoaquaponik.ui.viewmodel.SharedViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun FishPanelPage(
    sharedViewModel: SharedViewModel,
    viewModel: FishPanelViewModel = viewModel()
) {
    val kolamId by sharedViewModel.selectedKolamId
    val records by viewModel.records.collectAsState()

    var jumlahIkan by remember { mutableStateOf("") }
    var pakanGram by remember { mutableStateOf("") }
    var mortalitas by remember { mutableStateOf("") }
    var catatan by remember { mutableStateOf("") }
    var errorTextRes by remember { mutableIntStateOf(0) }

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
                label = stringResource(R.string.fish_panel_total_record),
                value = records.firstOrNull()?.jumlahIkan?.toString() ?: "0",
                unit = stringResource(R.string.fish_unit_tail),
                icon = Icons.Default.WaterDrop,
                isNormal = records.firstOrNull()?.mortalitas.orZero() == 0
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.fish_panel_form_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A2E)
                    )

                    OutlinedTextField(
                        value = jumlahIkan,
                        onValueChange = { jumlahIkan = it.filter(Char::isDigit) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(R.string.fish_panel_total_label)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = pakanGram,
                        onValueChange = { pakanGram = it.filter(Char::isDigit) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(R.string.fish_panel_feed_label)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = mortalitas,
                        onValueChange = { mortalitas = it.filter(Char::isDigit) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(R.string.fish_panel_mortality_label)) },
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
                            val jumlah = jumlahIkan.toIntOrNull()
                            val pakan = pakanGram.toIntOrNull()
                            val mati = mortalitas.toIntOrNull() ?: 0
                            if (jumlah == null || pakan == null) {
                                errorTextRes = R.string.form_error_required_numbers
                            } else {
                                viewModel.addRecord(
                                    kolamId = kolamId,
                                    jumlahIkan = jumlah,
                                    pakanGram = pakan,
                                    mortalitas = mati,
                                    catatan = catatan.trim()
                                )
                                jumlahIkan = ""; pakanGram = ""; mortalitas = ""; catatan = ""; errorTextRes = 0
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ToscaPrimary)
                    ) {
                        Text(
                            stringResource(R.string.action_save_fish_panel),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = stringResource(R.string.fish_panel_history_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A2E),
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        if (records.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.fish_panel_empty),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 24.dp)
                )
            }
        } else {
            items(records, key = { it.id }) { record ->
                FishPanelRecordCard(record)
            }
        }
    }
}

@Composable
private fun FishPanelRecordCard(record: FishPanelRecord) {
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
                text = formatRecordTime(record.createdAt),
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.fish_panel_total_value, record.jumlahIkan),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A2E)
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = ToscaPrimary.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = stringResource(R.string.fish_panel_feed_value, record.pakanGram),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelLarge,
                        color = ToscaPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Text(
                text = stringResource(R.string.fish_panel_mortality_value, record.mortalitas),
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

private fun formatRecordTime(timestamp: Long): String {
    if (timestamp <= 0L) return "-"
    return SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date(timestamp))
}

private fun Int?.orZero(): Int = this ?: 0