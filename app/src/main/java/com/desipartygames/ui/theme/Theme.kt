package com.desipartygames.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SleekLightColorScheme = lightColorScheme(
    primary = SleekPurple,
    onPrimary = Color.White,
    primaryContainer = SleekPurpleContainer,
    onPrimaryContainer = SleekOnPurpleContainer,
    secondary = SleekPurpleLight,
    onSecondary = Color.White,
    secondaryContainer = SleekRoseContainer,
    onSecondaryContainer = SleekOnRose,
    tertiary = PeacockTeal,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFCCE8E3),
    onTertiaryContainer = Color(0xFF00201C),
    background = SleekBg,
    onBackground = SleekTextPrimary,
    surface = SleekSurfaceCard,
    onSurface = SleekTextPrimary,
    surfaceVariant = SleekSurfaceElevated,
    onSurfaceVariant = SleekTextSecondary,
    outline = SleekSurfaceBorder,
    error = CrimsonRed,
    onError = Color.White
)

private val SleekDarkColorScheme = darkColorScheme(
    primary = SleekPurpleLight,
    onPrimary = Color(0xFF381E72),
    primaryContainer = SleekPurpleDark,
    onPrimaryContainer = SleekPurpleContainer,
    secondary = SleekRose,
    onSecondary = SleekOnRose,
    secondaryContainer = Color(0xFF4A3200),
    onSecondaryContainer = Color(0xFFFFE082),
    tertiary = PeacockTealLight,
    onTertiary = Color(0xFF003731),
    tertiaryContainer = PeacockTeal,
    onTertiaryContainer = Color(0xFFCCE8E3),
    background = Color(0xFF141218),
    onBackground = Color(0xFFE6E1E5),
    surface = Color(0xFF1D1B20),
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = Color(0xFF49454F),
    onSurfaceVariant = Color(0xFFCAC4D0),
    outline = Color(0xFF938F99),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) SleekDarkColorScheme else SleekLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun DesiPartyTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MyApplicationTheme(darkTheme = darkTheme, content = content)
}

