package com.cibiruwetan.protoaquaponik.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cibiruwetan.protoaquaponik.model.Sensor

@Composable
fun SensorItemCard(sensor: Sensor, modifier: Modifier = Modifier) {
    SensorMetricCard(
        label = sensor.name,
        value = sensor.value,
        unit = sensor.unit,
        icon = sensor.icon,
        isNormal = sensor.isNormal,
        modifier = modifier
    )
}
