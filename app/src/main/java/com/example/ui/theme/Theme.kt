package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = CyberCyan,
  onPrimary = Color(0xFF031024),
  primaryContainer = Color(0xFF0C2B54),
  onPrimaryContainer = Color(0xFFCBE6FF),
  secondary = CyberBlue,
  onSecondary = Color.White,
  secondaryContainer = Color(0xFF10284F),
  onSecondaryContainer = Color(0xFFD4E3FF),
  tertiary = CyberGreen,
  onTertiary = Color(0xFF003915),
  tertiaryContainer = Color(0xFF005322),
  onTertiaryContainer = Color(0xFF8CF6A8),
  background = CyberDarkBg,
  onBackground = TextPrimary,
  surface = CyberDarkSurface,
  onSurface = TextPrimary,
  surfaceVariant = CyberCardBg,
  onSurfaceVariant = TextSecondary,
  outline = CyberCardBorder,
  error = CyberRed,
  onError = Color.White
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = DarkColorScheme,
    typography = Typography,
    content = content
  )
}


