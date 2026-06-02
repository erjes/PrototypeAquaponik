package com.cibiruwetan.protoaquaponik.model

import androidx.compose.ui.graphics.vector.ImageVector

data class Sensor(
    val name: String,
    val value: String,
    val unit: String,
    val icon: ImageVector,
    val isNormal: Boolean
)
