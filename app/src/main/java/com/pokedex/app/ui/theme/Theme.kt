package com.pokedex.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(primary = Color(0xFFD32F2F), secondary = Color(0xFFFF7043), tertiary = Color(0xFF1565C0), background = Color(0xFFFFFBFF), surface = Color(0xFFFFFBFF))
private val DarkColors = darkColorScheme(primary = Color(0xFFFFB4AB), secondary = Color(0xFFFFB59E), tertiary = Color(0xFFA9C7FF))
@Composable fun PokedexTheme(content: @Composable () -> Unit) = MaterialTheme(colorScheme = LightColors, typography = androidx.compose.material3.Typography(), content = content)
