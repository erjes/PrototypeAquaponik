package com.cibiruwetan.protoaquaponik.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


private val LightColorScheme = lightColorScheme(
    primary = ToscaPrimary,
    onPrimary = CardWhite,
    background = BackgroundLight,
    surface = CardWhite,
    onBackground = TextDark
)

@Composable
fun AquaponikTheme(content: @Composable () -> Unit) {
        MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}