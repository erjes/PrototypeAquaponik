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
import androidx.compose.material.icons.filled.WaterDrop
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
import com.cibiruwetan.protoaquaponik.model.FishPanelRecord
import com.cibiruwetan.protoaquaponik.ui.components.SensorMetricCard
import com.cibiruwetan.protoaquaponik.ui.theme.BackgroundLight
import com.cibiruwetan.protoaquaponik.ui.viewmodel.FishPanelViewModel
import com.cibiruwetan.protoaquaponik.ui.viewmodel.SharedViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
            FishPanelForm(
                jumlahIkan = jumlahIkan,
                pakanGram = pakanGram,
                mortalitas = mortalitas,
                catatan = catatan,
                errorTextRes = errorTextRes,
                onJumlahIkanChange = { jumlahIkan = it.filter(Char::isDigit) },
                onPakanGramChange = { pakanGram = it.filter(Char::isDigit) },
                onMortalitasChange = { mortalitas = it.filter(Char::isDigit) },
                onCatatanChange = { catatan = it },
                onSaveClick = {
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
                        jumlahIkan = ""
                        pakanGram = ""
                        mortalitas = ""
                        catatan = ""
                        errorTextRes = 0
                    }
                }
            )
        }

        item {
            Text(
                text = stringResource(R.string.fish_panel_history_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
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
private fun FishPanelForm(
    jumlahIkan: String,
    pakanGram: String,
    mortalitas: String,
    catatan: String,
    errorTextRes: Int,
    onJumlahIkanChange: (String) -> Unit,
    onPakanGramChange: (String) -> Unit,
    onMortalitasChange: (String) -> Unit,
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
                text = stringResource(R.string.fish_panel_form_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = jumlahIkan,
                onValueChange = onJumlahIkanChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.fish_panel_total_label)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            OutlinedTextField(
                value = pakanGram,
                onValueChange = onPakanGramChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.fish_panel_feed_label)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            OutlinedTextField(
                value = mortalitas,
                onValueChange = onMortalitasChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.fish_panel_mortality_label)) },
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
                Text(stringResource(R.string.action_save_fish_panel))
            }
        }
    }
}

@Composable
private fun FishPanelRecordCard(record: FishPanelRecord) {
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
                text = formatRecordTime(record.createdAt),
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.fish_panel_total_value, record.jumlahIkan),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.fish_panel_feed_value, record.pakanGram),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Text(
                text = stringResource(R.string.fish_panel_mortality_value, record.mortalitas),
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

private fun formatRecordTime(timestamp: Long): String {
    if (timestamp <= 0L) return "-"
    return SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date(timestamp))
}

private fun Int?.orZero(): Int = this ?: 0
