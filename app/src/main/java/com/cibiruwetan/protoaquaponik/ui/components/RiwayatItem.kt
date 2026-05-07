package com.cibiruwetan.protoaquaponik.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cibiruwetan.protoaquaponik.model.Riwayat
import com.cibiruwetan.protoaquaponik.ui.theme.StatusGreen
import com.cibiruwetan.protoaquaponik.ui.theme.StatusRed


@Composable
fun RiwayatItem(riwayat: Riwayat, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(riwayat.tanggal, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                Text("${riwayat.ppm} PPM", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            Surface(
                color = if(riwayat.statusAir == "Stabil") StatusGreen.copy(0.1f) else StatusRed.copy(0.1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    riwayat.statusAir,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = if(riwayat.statusAir == "Stabil") StatusGreen else StatusRed,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}