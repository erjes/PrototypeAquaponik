package com.cibiruwetan.protoaquaponik.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.cibiruwetan.protoaquaponik.model.Kolam
import com.cibiruwetan.protoaquaponik.ui.theme.ToscaPrimary

@Composable
fun KolamCard(kolam: Kolam, onClick: () -> Unit) {
    val tdsIsNormal = kolam.tds_simulasi.isTdsNormal()
    val statusAirLabel = kolam.statusAir.ifBlank {
        if (tdsIsNormal) stringResource(R.string.status_normal) else stringResource(R.string.status_abnormal)
    }

    ElevatedCard(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = ToscaPrimary.copy(alpha = 0.1f),
                    shape = CircleShape,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.WaterDrop,
                        contentDescription = stringResource(R.string.content_description_pond_selector),
                        tint = ToscaPrimary,
                        modifier = Modifier.padding(12.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = kolam.id,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = kolam.lokasi,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Text(
                        text = statusAirLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = ToscaPrimary
                    )
                }
            }

            StatusBadge(
                isNormal = kolam.isAktif,
                normalText = stringResource(R.string.status_aktif),
                abnormalText = stringResource(R.string.status_nonaktif)
            )
        }
    }
}
