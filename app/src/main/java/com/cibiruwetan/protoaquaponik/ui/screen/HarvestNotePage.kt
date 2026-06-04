package com.cibiruwetan.protoaquaponik.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cibiruwetan.protoaquaponik.model.HarvestNote
import com.cibiruwetan.protoaquaponik.ui.theme.BackgroundLight
import com.cibiruwetan.protoaquaponik.ui.viewmodel.HarvestNoteViewModel
import com.cibiruwetan.protoaquaponik.ui.viewmodel.SharedViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HarvestNotePage(
    sharedViewModel: SharedViewModel,
    viewModel: HarvestNoteViewModel = viewModel()
) {
    val kolamId by sharedViewModel.selectedKolamId
    val notes by viewModel.notes.collectAsState()

    // Form state
    var jenisPanen by remember { mutableStateOf("ikan") }
    var namaPanen by remember { mutableStateOf("") }
    var beratKg by remember { mutableStateOf("") }
    var tanggalPanen by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }

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
        // Form tambah catatan
        item {
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
                        text = "Tambah Catatan Panen",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    // Toggle jenis panen
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = jenisPanen == "ikan",
                            onClick = { jenisPanen = "ikan" },
                            label = { Text("🐟 Ikan") },
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = jenisPanen == "tumbuhan",
                            onClick = { jenisPanen = "tumbuhan" },
                            label = { Text("🌿 Tumbuhan") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    OutlinedTextField(
                        value = namaPanen,
                        onValueChange = { namaPanen = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(if (jenisPanen == "ikan") "Nama Ikan" else "Nama Tanaman")
                        },
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = beratKg,
                        onValueChange = { beratKg = it.filter { c -> c.isDigit() || c == '.' || c == ',' } },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Berat Panen (kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = tanggalPanen,
                        onValueChange = { tanggalPanen = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Tanggal Panen (dd/MM/yyyy)") },
                        placeholder = { Text("contoh: 04/06/2026") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = deskripsi,
                        onValueChange = { deskripsi = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Deskripsi") },
                        minLines = 2
                    )

                    if (errorMsg.isNotBlank()) {
                        Text(
                            text = errorMsg,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    Button(
                        onClick = {
                            val berat = beratKg.replace(',', '.').toDoubleOrNull()
                            val nama = namaPanen.trim()
                            val tgl = parseTanggal(tanggalPanen.trim())

                            when {
                                nama.isBlank() -> errorMsg = "Nama panen wajib diisi"
                                berat == null -> errorMsg = "Berat harus berupa angka"
                                tgl == null -> errorMsg = "Format tanggal: dd/MM/yyyy"
                                else -> {
                                    viewModel.addNote(
                                        kolamId = kolamId,
                                        jenisPanen = jenisPanen,
                                        namaPanen = nama,
                                        beratKg = berat,
                                        tanggalPanen = tgl,
                                        deskripsi = deskripsi.trim()
                                    )
                                    namaPanen = ""
                                    beratKg = ""
                                    tanggalPanen = ""
                                    deskripsi = ""
                                    errorMsg = ""
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Simpan Catatan")
                    }
                }
            }
        }

        // Header riwayat
        item {
            Text(
                text = "Riwayat Catatan Panen",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // List catatan
        if (notes.isEmpty()) {
            item {
                Text(
                    text = "Belum ada catatan panen",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 24.dp)
                )
            }
        } else {
            items(notes, key = { it.id }) { note ->
                HarvestNoteCard(note)
            }
        }
    }
}

@Composable
private fun HarvestNoteCard(note: HarvestNote) {
    val isIkan = note.jenisPanen == "ikan"
    val chipColor = if (isIkan) Color(0xFF1976D2) else Color(0xFF388E3C)
    val emoji = if (isIkan) "🐟" else "🌿"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = chipColor.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = "$emoji ${note.jenisPanen.replaceFirstChar { it.uppercase() }}",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = chipColor,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = formatTanggal(note.tanggalPanen),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray
                )
            }
            Text(
                text = note.namaPanen,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${note.beratKg} kg",
                style = MaterialTheme.typography.bodyMedium,
                color = chipColor
            )
            if (note.deskripsi.isNotBlank()) {
                Text(
                    text = note.deskripsi,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}

private fun parseTanggal(input: String): Long? {
    return try {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(input)?.time
    } catch (e: Exception) {
        null
    }
}

private fun formatTanggal(timestamp: Long): String {
    if (timestamp <= 0L) return "-"
    return SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(timestamp))
}