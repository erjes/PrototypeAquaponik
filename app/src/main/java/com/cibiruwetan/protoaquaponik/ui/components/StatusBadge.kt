package com.cibiruwetan.protoaquaponik.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
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
import com.cibiruwetan.protoaquaponik.ui.theme.StatusGreen
import com.cibiruwetan.protoaquaponik.ui.theme.StatusRed

@Composable
fun StatusBadge(
    isNormal: Boolean,
    modifier: Modifier = Modifier,
    normalText: String = stringResource(R.string.status_normal),
    abnormalText: String = stringResource(R.string.status_abnormal)
) {
    val color = if (isNormal) StatusGreen else StatusRed
    val label = if (isNormal) normalText else abnormalText
    val icon = if (isNormal) Icons.Default.CheckCircle else Icons.Default.Warning

    Surface(
        modifier = modifier,
        color = color.copy(alpha = 0.14f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier
                    .padding(end = 6.dp)
                    .size(16.dp)
            )
            Text(
                text = label,
                color = color,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
