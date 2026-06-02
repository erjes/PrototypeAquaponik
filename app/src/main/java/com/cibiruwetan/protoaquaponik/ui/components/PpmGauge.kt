package com.cibiruwetan.protoaquaponik.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cibiruwetan.protoaquaponik.R
import com.cibiruwetan.protoaquaponik.data.isTdsNormal
import com.cibiruwetan.protoaquaponik.ui.theme.StatusRed
import com.cibiruwetan.protoaquaponik.ui.theme.ToscaPrimary

@Composable
fun PpmGauge(value: Int, maxValue: Int = 1000) {
    val boundedValue = value.coerceIn(0, maxValue)
    val sweepAngle = (boundedValue.toFloat() / maxValue) * 360f
    val progressColor = if (value.isTdsNormal()) ToscaPrimary else StatusRed

    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(150.dp)) {
        Canvas(modifier = Modifier.size(150.dp)) {
            drawArc(
                color = Color.LightGray.copy(alpha = 0.3f),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = stringResource(R.string.label_ppm), style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Text(text = "$value", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
        }
    }
}
