package com.cibiruwetan.protoaquaponik.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cibiruwetan.protoaquaponik.R
import com.cibiruwetan.protoaquaponik.data.isTdsNormal
import com.cibiruwetan.protoaquaponik.model.Riwayat


@Composable
fun RiwayatItem(riwayat: Riwayat, onClick: () -> Unit) {
    val tdsIsNormal = riwayat.ppm.isTdsNormal()
    val statusText = riwayat.statusAir.ifBlank {
        if (tdsIsNormal) stringResource(R.string.status_normal) else stringResource(R.string.status_abnormal)
    }

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
                Text(
                    text = stringResource(R.string.riwayat_ppm_value, riwayat.ppm),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            StatusBadge(
                isNormal = tdsIsNormal,
                normalText = statusText,
                abnormalText = statusText
            )
        }
    }
}
